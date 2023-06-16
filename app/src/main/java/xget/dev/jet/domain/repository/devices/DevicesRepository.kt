package xget.dev.jet.domain.repository.devices

import kotlinx.coroutines.flow.Flow
import xget.dev.jet.domain.model.device.SmartDevice

interface DevicesRepository {

    suspend fun getDevice(uid: String): Flow<SmartDevice>
    suspend fun getDevicesByUserId(uid: String): Flow<List<SmartDevice>>
}