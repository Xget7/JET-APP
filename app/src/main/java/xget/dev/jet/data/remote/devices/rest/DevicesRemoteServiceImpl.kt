package xget.dev.jet.data.remote.devices.rest

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import xget.dev.jet.data.remote.HttpRoutes.CREATE_DEVICE

import xget.dev.jet.data.remote.HttpRoutes.GET_DEVICE
import xget.dev.jet.data.remote.HttpRoutes.GET_DEVICES_FROM_USER
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.util.network.handleApiException
import xget.dev.jet.domain.repository.devices.rest.DevicesListRestResponse
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject



class DevicesRemoteServiceImpl @Inject constructor(
  val client : HttpClient ,
    val token: Token
) : DevicesRemoteService{
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

    override suspend fun createDevice(request: DeviceDto): ApiResponse<DeviceDto> {
        ApiResponse.Loading<Any>()
        return try {
            val response = client.post {
                url(CREATE_DEVICE)
                setBody(request)
                header("Authorization", "Bearer ${token.getJwtLocal()}")
            }.body<DeviceDto>()
            ApiResponse.Success(response)
        } catch (e: Exception) {
            handleApiException(e)
        }
    }

}