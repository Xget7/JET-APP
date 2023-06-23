package xget.dev.jet.data.remote.devices.rest

import android.util.Log
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import xget.dev.jet.data.remote.HttpRoutes.CREATE_DEVICE

import xget.dev.jet.data.remote.HttpRoutes.GET_DEVICE
import xget.dev.jet.data.remote.HttpRoutes.GET_DEVICES_FROM_USER
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.util.network.handleApiCodeStatusException
import xget.dev.jet.data.util.network.handleApiException
import xget.dev.jet.domain.repository.devices.rest.DevicesListRestResponse
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject


class DevicesRemoteServiceImpl @Inject constructor(
    val client: HttpClient,
    val token: Token
) : DevicesRemoteService {
    override suspend fun getDeviceById(id: String): ApiResponse<DeviceDto> {
        return try {
            val response = client.get {
                url(GET_DEVICE)
                parameter("id", id)
                header("Authorization", "Bearer ${token.getJwtLocal()}")
            }.body<DeviceDto>()
            ApiResponse.Success(response)
        } catch (e: Exception) {
            handleApiException(e)
        }
    }

    override suspend fun getDevicesByUserUid(uid: String): DevicesListRestResponse {
        return try {
            val response = client.get {
                url(GET_DEVICES_FROM_USER)
                parameter("uid", uid)
                header("Authorization", "Bearer ${token.getJwtLocal()}")
            }.body<List<DeviceDto>>()
            ApiResponse.Success(response)
        } catch (e: Exception) {
            handleApiException(e)
        }
    }

    override  fun createDevice(request: DeviceDto): Flow<ApiResponse<Boolean>> = callbackFlow {
        try {
            val response = client.post(CREATE_DEVICE) {
                contentType(ContentType.Application.Json) // Set the content type to JSON
                setBody(request)
            }


            if (response.status != HttpStatusCode.Created){
                trySend(handleApiCodeStatusException(response.status))
                close()
            }else{
                trySend(ApiResponse.Success(true))
                close()
            }
        } catch (e: Exception) {
            Log.d("createDevice Error?/", e.localizedMessage , e)
            trySend(handleApiException(e))
            close()
        }
        awaitClose {}
    }.flowOn(Dispatchers.IO)

}