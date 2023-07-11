package xget.dev.jet.data.remote.devices.rest

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import xget.dev.jet.data.remote.HttpRoutes.CREATE_DEVICE

import xget.dev.jet.data.remote.HttpRoutes.BASE_DEVICE
import xget.dev.jet.data.remote.HttpRoutes.DEVICE_HISTORY
import xget.dev.jet.data.remote.HttpRoutes.GET_DEVICES_FROM_USER
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.remote.devices.rest.dto.DevicesListResponse
import xget.dev.jet.data.remote.devices.rest.dto.history.DeviceActionReq
import xget.dev.jet.data.remote.devices.rest.dto.history.DeviceHistoryResponse
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.util.network.handleApiCodeStatusException
import xget.dev.jet.data.util.network.handleApiException
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.domain.repository.token.Token
import javax.annotation.Nullable
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class DevicesRemoteServiceImpl @Inject constructor(
    val client: HttpClient,
    val token: Token
) : DevicesRemoteService {
    override suspend fun getDeviceById(id: String): ApiResponse<DeviceDto> {
        return try {
            val response = client.get {
                url(BASE_DEVICE)
                parameter("id", id)
                header("Authorization", "Bearer ${token.getJwtLocal()}")
            }.body<DeviceDto>()

            ApiResponse.Success(response)
        } catch (e: Exception) {
            handleApiException(e)
        }
    }

    override fun getDevicesByUser() = callbackFlow {
        try {
            Log.d("trying response", "yes")
            Log.d("localToken", token.getJwtLocal().toString())
            val response = client.get(GET_DEVICES_FROM_USER) {
                header("Authorization", "Bearer ${token.getJwtLocal()}")
            }
            Log.d("getDeviceSBYUserREsponse", response.body())
            trySend(ApiResponse.Success(response.body<DevicesListResponse>()))
            close()
        } catch (e: Exception) {
            trySend(handleApiException(e))
            close()
            cancel()
        }
        awaitClose { }
    }

    override suspend fun getDeviceHistory(id: String): ApiResponse<DeviceHistoryResponse> =   suspendCoroutine {continuation ->
        try {
            val payload = mapOf("id_device" to id)
            CoroutineScope(continuation.context).launch {
                val response = client.post(DEVICE_HISTORY) {
                    header("Authorization", "Bearer ${token.getJwtLocal()}")
                    contentType(ContentType.Application.Json) // Set the content type to JSON
                    setBody(payload)
                }
                val parsedResponse = response.body<DeviceHistoryResponse>()

                if (response.status != HttpStatusCode.OK) {
                    continuation.resume(handleApiCodeStatusException(response.status))
                } else {
                    continuation.resume(ApiResponse.Success(parsedResponse))
                }
            }

        } catch (e: Exception) {
            Log.d("getDeviceHistory Error?/", e.localizedMessage, e)
            continuation.resumeWithException(e)
        }
    }

    override fun createDevice(request: DeviceDto): Flow<ApiResponse<Boolean>> = callbackFlow {
        try {
            val response = client.post(CREATE_DEVICE) {
                contentType(ContentType.Application.Json) // Set the content type to JSON
                setBody(request)
                header("Authorization", "Bearer ${token.getJwtLocal()}")
            }


            if (response.status != HttpStatusCode.Created) {
                trySend(handleApiCodeStatusException(response.status))
                close()
            } else {
                trySend(ApiResponse.Success(true))
                close()
            }
        } catch (e: Exception) {
            Log.d("createDevice Error?/", e.localizedMessage, e)
            trySend(handleApiException(e))
            close()
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)

    override suspend fun uploadDeviceAction(req : DeviceActionReq): ApiResponse<Nullable> =
        suspendCoroutine { continuation ->
            try {
                CoroutineScope(continuation.context).launch {
                    val response = client.post(DEVICE_HISTORY) {
                        header("Authorization", "Bearer ${token.getJwtLocal()}")
                        setBody(req)
                    }
                    if (response.status == HttpStatusCode.Created) {
                        continuation.resume(ApiResponse.Success(Nullable()))
                    }
                }
            } catch (e: Exception) {
                continuation.resume(ApiResponse.Error("Error de conexion."))
            }
    }

    override suspend fun deleteDevice(deviceId: String): ApiResponse<Boolean> =
        suspendCoroutine { continuation ->
            try {
                CoroutineScope(continuation.context).launch {

                    val response = client.patch("$BASE_DEVICE/$deviceId/") {
                        header("Authorization", "Bearer ${token.getJwtLocal()}")
                    }
                    if (response.status == HttpStatusCode.NoContent) {
                        continuation.resume(ApiResponse.Success(true))
                    } else {
                        continuation.resume(ApiResponse.Error("Error intentando eliminar dispositivo."))
                    }
                }
            } catch (e: Exception) {
                continuation.resume(ApiResponse.Error("Error al intentar eliminar el dispositivo."))
            }
        }

    override suspend fun updateDevice(device: DeviceDto): ApiResponse<Boolean> = suspendCoroutine { continuation ->
            try {
                CoroutineScope(continuation.context).launch {

                    val response = client.put("$BASE_DEVICE/${device.id}/") {
                        header("Authorization", "Bearer ${token.getJwtLocal()}")
                        setBody(device)
                    }
                    if (response.status == HttpStatusCode.OK) {
                        continuation.resume(ApiResponse.Success(true))
                    } else {
                        continuation.resume(ApiResponse.Error("Error intentando actualizar dispositivo."))
                    }
                }
            } catch (e: Exception) {
                continuation.resume(ApiResponse.Error("Error al intentar actualizar el dispositivo."))
            }
    }

    override suspend fun addUserToDevice( accessUserEmail: String,deviceId: String): ApiResponse<Boolean>  =  suspendCoroutine { continuation ->

        val email = accessUserEmail
        try {
            CoroutineScope(continuation.context).launch {
                val response = client.patch("$BASE_DEVICE/$deviceId/") {
                    header("Authorization", "Bearer ${token.getJwtLocal()}")
                    setBody(email)
                }
                if (response.status == HttpStatusCode.OK) {
                    continuation.resume(ApiResponse.Success(true))
                } else {
                    continuation.resume(ApiResponse.Error("Error intentando añadir usuario aldispositivo."))
                }
            }


        } catch (e: Exception) {
            Log.d("exception",e.localizedMessage,e)
            continuation.resume(ApiResponse.Error("Error intentando añadir usuario aldispositivo."))
        }
    }

}