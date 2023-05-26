package xget.dev.jet.domain.model.mqtt

import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.Date

class ReceivedMessage(val topic: String, val message: MqttMessage) {
    val timestamp: Date = Date()
    override fun toString(): String {
        return "Received{" +
                "topic='" + topic + '\'' +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}'
    }

}