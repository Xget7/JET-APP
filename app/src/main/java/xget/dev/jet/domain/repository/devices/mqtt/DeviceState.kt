package xget.dev.jet.domain.repository.devices.mqtt

import org.eclipse.paho.client.mqttv3.MqttTopic
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt

sealed class DeviceState {
    data class Success(val device: SmartDeviceMqtt  , val topic: String) : DeviceState()
    data class DisconnectedOrError(val message : String?) : DeviceState()
}