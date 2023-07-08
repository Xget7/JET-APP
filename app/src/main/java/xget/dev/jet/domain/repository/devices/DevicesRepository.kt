package xget.dev.jet.domain.repository.devices

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import xget.dev.jet.domain.model.device.SmartDevice

interface DevicesRepository {


    fun getDevice(id: String, userId : String): Flow<SmartDevice>
    fun initRepo(): Flow<Boolean>
    suspend fun releaseMqtt()
    fun getDevicesByUserId(uid: String): Flow<List<SmartDevice>>
    suspend fun sendMessageToDevice(deviceId: String, state: Int, userId : String): Boolean}