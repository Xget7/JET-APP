package xget.dev.jet.domain.repository.devices

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import xget.dev.jet.domain.model.device.SmartDevice

interface DevicesRepository {


    suspend fun getDevice(uid: String): Flow<SmartDevice>
    fun getDevicesByUserId(uid: String): Flow<List<SmartDevice>>
}