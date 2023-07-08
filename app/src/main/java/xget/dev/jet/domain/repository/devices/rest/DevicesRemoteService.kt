package xget.dev.jet.domain.repository.devices.rest

import kotlinx.coroutines.flow.Flow
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.remote.devices.rest.dto.DevicesListResponse
import xget.dev.jet.data.remote.devices.rest.dto.history.DeviceActionRequest
import xget.dev.jet.data.util.network.ApiResponse
import javax.annotation.Nullable

typealias DevicesListRestResponse = ApiResponse<List<DeviceDto>>
typealias DeviceRestResponse = ApiResponse<DeviceDto>
interface DevicesRemoteService {

    suspend fun getDeviceById(id: String): DeviceRestResponse
    fun getDevicesByUser(): Flow<ApiResponse<DevicesListResponse>>
    fun createDevice(request: DeviceDto): Flow<ApiResponse<Boolean>>

    suspend fun uploadDeviceAction(req : DeviceActionRequest) : ApiResponse<Nullable>

    suspend fun deleteDevice(deviceId : String) : ApiResponse<Boolean>
    suspend fun updateDevice(device : DeviceDto) : ApiResponse<Boolean>


    suspend fun addUserToDevice(accessUserEmail: String, deviceId: String): ApiResponse<Boolean>
}
