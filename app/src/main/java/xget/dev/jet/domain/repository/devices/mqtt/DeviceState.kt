package xget.dev.jet.domain.repository.devices.mqtt

import org.eclipse.paho.client.mqttv3.MqttTopic

sealed class DeviceState {
    data class Success(val message : String, val topic: String) : DeviceState()
    data class DisconnectedOrError(val message : String?) : DeviceState()
}