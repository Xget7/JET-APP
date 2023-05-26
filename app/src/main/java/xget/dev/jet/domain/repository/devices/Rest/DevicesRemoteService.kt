package xget.dev.jet.domain.repository.devices.Rest

import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.util.network.ApiResponse

interface DevicesRemoteService {

    suspend fun getDeviceById(id : String) : ApiResponse<DeviceDto>
    suspend fun getDevicesByUserUid(uid : String) : DevicesListRestResponse


}

typealias DevicesListRestResponse = ApiResponse<List<DeviceDto>>