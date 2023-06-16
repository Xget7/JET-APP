package xget.dev.jet.domain.repository.devices.mqtt

import kotlinx.coroutines.flow.SharedFlow

interface DevicesMqttService {

    val errors: SharedFlow<String>
}