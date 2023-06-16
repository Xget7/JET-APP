package xget.dev.jet.domain.model.mqtt

import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.Date

data class ReceivedMessage(
    val connection : Boolean = true,
    val message : String ="" ,
    val topic : String = "",
    val error : String? = null
)

