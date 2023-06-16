package xget.dev.jet.domain.repository.devices.rest

import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.util.network.ApiResponse

typealias DevicesListRestResponse = ApiResponse<List<DeviceDto>>
typealias DeviceRestResponse = ApiResponse<DeviceDto>
interface DevicesRemoteService {

    suspend fun getDeviceById(id: String): DeviceRestResponse
    suspend fun getDevicesByUserUid(uid: String): DevicesListRestResponse
    suspend fun createDevice(request: DeviceDto): DeviceRestResponse


}
