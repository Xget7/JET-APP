package xget.dev.jet.domain.repository.devices.mqtt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt

interface DevicesMqttService {

    val errors: SharedFlow<String>

    suspend fun collectMqttErrors()

    fun subscribeAndCollectDevices(devicesId: List<String>): Flow<List<SmartDeviceMqtt>>
    suspend fun sendMessageToDevice(deviceId: String, data: String): Boolean

    suspend fun release()
}