package xget.dev.jet.domain.services.mqtt

import android.content.Context
import android.content.Intent
import android.util.Log
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import xget.dev.jet.MainActivity
import xget.dev.jet.R
import xget.dev.jet.domain.services.mqtt.MqttConnections.Companion.getInstance
import xget.dev.jet.domain.services.mqtt.Notify.notification


internal class MqttCallbackHandler(private val context: Context, private val clientHandle: String) : MqttCallback {

    override fun connectionLost(cause: Throwable?) {
        Log.d("Connection Lost: ", "${cause?.message}")
        val connection = getInstance(context).getConnection(clientHandle)
        connection?.addHistory("Connection Lost")
        connection?.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED)

        val intent = Intent()
        intent.setClassName(context, activityClass)
        intent.putExtra("handle", clientHandle)

        notification(context, "id=${connection?.id} host=${connection?.hostName}", intent, R.string.notifyTitle_connectionLost)
    }

    @Throws(Exception::class)
    override fun messageArrived(topic: String, message: MqttMessage) {
        val messageString = "${message.payload} $topic qos=${message.qos} retained:${message.isRetained}"
        Log.i("Timber I",messageString)

        //Get connection object associated with this object
        getInstance(context).getConnection(clientHandle)?.apply {
            addMessage(topic, message)
        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken) {
        getInstance(context).getConnection(clientHandle)?.apply {
            addHistory("deliveryComplete ${token.message}")
        }
    }

    companion object {
        private val activityClass = MainActivity::class.java.name
    }
}