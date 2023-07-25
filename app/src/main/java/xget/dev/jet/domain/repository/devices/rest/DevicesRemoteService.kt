package xget.dev.jet.domain.repository.devices.rest

import kotlinx.coroutines.flow.Flow
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.remote.devices.rest.dto.DevicesListResponse
import xget.dev.jet.data.remote.devices.rest.dto.history.DeviceActionReq
import xget.dev.jet.data.remote.devices.rest.dto.history.DeviceHistoryResponse
import xget.dev.jet.data.util.network.ApiResponse
import javax.annotation.Nullable

typealias DevicesListRestResponse = ApiResponse<List<DeviceDto>>
typealias DeviceRestResponse = ApiResponse<DeviceDto>
interface DevicesRemoteService {

    suspend fun getDeviceById(id: String): DeviceRestResponse
    fun getDevicesByUser(): Flow<ApiResponse<DevicesListResponse>>

    suspend fun getDeviceHistory(id : String) : ApiResponse<DeviceHistoryResponse>

    fun createDevice(request: DeviceDto): Flow<ApiResponse<Boolean>>

    suspend fun uploadDeviceAction(req : DeviceActionReq) : ApiResponse<Boolean>

    suspend fun deleteDevice(deviceId : String) : ApiResponse<Boolean>
    suspend fun updateDevice(device : DeviceDto) : ApiResponse<Boolean>

    suspend fun addUserToDevice(accessUserEmail: String, deviceId: String): ApiResponse<Boolean>
}
