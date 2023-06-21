package xget.dev.jet.domain.repository.devices.rest

import kotlinx.coroutines.flow.Flow
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.util.network.ApiResponse

typealias DevicesListRestResponse = ApiResponse<List<DeviceDto>>
typealias DeviceRestResponse = ApiResponse<DeviceDto>
interface DevicesRemoteService {

    suspend fun getDeviceById(id: String): DeviceRestResponse
    suspend fun getDevicesByUserUid(uid: String): DevicesListRestResponse
    fun createDevice(request: DeviceDto): Flow<ApiResponse<Boolean>>


}
