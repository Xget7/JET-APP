package xget.dev.jet.domain.repository.devices.mqtt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt

interface DevicesMqttService {

    val errors: SharedFlow<String>

    suspend fun collectMqttErrors()
    fun init(): Flow<Boolean>

    fun subscribeAndCollectDevices(
        devicesId: List<String>,
        userId: String
    ): Flow<List<Pair<String, SmartDeviceMqtt>>>

    fun subscribeAndCollectOneDevice(deviceId: String,userDeviceId : String): Flow<Pair<String, SmartDeviceMqtt>>
    suspend fun sendMessageToDevice(deviceId: String, data: String,userId: String) : Boolean
    suspend fun release()
}