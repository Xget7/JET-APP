package xget.dev.jet.domain.repository.devices.mqtt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface DevicesMqttService {

    val errors: SharedFlow<String>

    suspend fun collectMqttErrors()

    fun subscribeAndCollectDevices(userId: String, devicesId: List<String>): Flow<List<DeviceState>>
}