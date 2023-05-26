package xget.dev.jet.data.local.entity.utils

import android.content.Context
import info.mqtt.android.service.QoS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import xget.dev.jet.data.local.db.AppDatabase
import xget.dev.jet.data.local.entity.ConnectionEntity
import xget.dev.jet.domain.services.mqtt.Action
import xget.dev.jet.domain.services.mqtt.ActionListener
import xget.dev.jet.domain.services.mqtt.Connection
import xget.dev.jet.domain.services.mqtt.MqttCallbackHandler

fun Connection.connect(context: Context) {
    val actionArgs = arrayOfNulls<String>(1)
    actionArgs[0] = this.id
    val callback = ActionListener(context, Action.CONNECT, this, *actionArgs)
    this.client.setCallback(MqttCallbackHandler(context, this.handle()))
    this.client.connect(this.connectionOptions, null, callback)
}

fun Boolean.toInt() = if (this) 1 else 0

fun Int.toBoolean() = this == 1

fun ConnectionEntity.toConnection(context: Context): Connection {
    //rebuild objects starting with the connect options
    val connectOptions = MqttConnectOptions()
    connectOptions.isCleanSession = cleanSession.toBoolean()
    connectOptions.keepAliveInterval = keepAlive
    connectOptions.connectionTimeout = timeout
    connectOptions.password = userPass?.toCharArray() ?: "".toCharArray()
    connectOptions.userName = userName
    this.topic?.let {
        connectOptions.setWill(it, message.toByteArray(), qos.value, retained.toBoolean())
    }

    val connection = Connection.createConnection(
        clientHandle, clientId, host,
        port, context, ssl.toBoolean(),
        connectOptions
    )

    // Now we recover all subscriptions for this connection
    CoroutineScope(Dispatchers.IO).launch {
        connection.setSubscriptions(AppDatabase.getDatabase(context).subscriptionDao().all.map { it.toSubscription() })
    }
    return connection
}

fun Connection.toConnectionEntity(): ConnectionEntity = ConnectionEntity(
    handle(),
    hostName,
    id,
    port,
    isSSL,
    connectionOptions.connectionTimeout,
    connectionOptions.keepAliveInterval,
    connectionOptions.userName,
    String(connectionOptions.password ?: CharArray(0)),
    connectionOptions.isCleanSession.toInt(),
    connectionOptions.willDestination,
    connectionOptions.willMessage?.payload.toString(), // message
    QoS.valueOf(connectionOptions.willMessage?.qos ?: QoS.AtMostOnce.value),
    connectionOptions.willMessage?.isRetained?.toInt() ?: 0
)