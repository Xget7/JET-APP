package xget.dev.jet.domain.services.mqtt

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import xget.dev.jet.MainActivity
import xget.dev.jet.domain.services.mqtt.MqttConnections.Companion.getInstance
import xget.dev.jet.domain.services.mqtt.Notify.toast

class ActionListener(
    private val context: Context,
    private val action: Action, private val connection: Connection, vararg additionalArgs: String?
) : IMqttActionListener {

    private val additionalArgs: Array<String?> = arrayOf(*additionalArgs)
    private val clientHandle: String = connection.handle()

    override fun onSuccess(asyncActionToken: IMqttToken) {
        when (action) {
            Action.CONNECT -> connect()
            Action.DISCONNECT -> disconnect()
            Action.SUBSCRIBE -> subscribe()
            Action.PUBLISH -> publishNotify()
        }
    }

    /**
     * A publish action has been successfully completed, update connection
     * object associated with the client this action belongs to, then notify the
     * user of success
     */
    private fun publishNotify() {
        val connection = getInstance(context).getConnection(clientHandle)

        @SuppressLint("StringFormatMatches")
        val actionTaken = "publish notify"
        connection!!.addHistory(actionTaken)
        toast(context, actionTaken, Toast.LENGTH_SHORT)
    }

    /**
     * A addNewSubscription action has been successfully completed, update the connection
     * object associated with the client this action belongs to and then notify
     * the user of success
     */
    private fun subscribe() {
        val connection = getInstance(context).getConnection(clientHandle)
        val actionTaken = "sub"
        connection!!.addHistory(actionTaken)
        toast(context, actionTaken, Toast.LENGTH_SHORT)
        print(actionTaken)
    }

    /**
     * A disconnection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private fun disconnect() {
        val connection = getInstance(context).getConnection(clientHandle)
        connection!!.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED)
        val actionTaken = "Disconected"
        connection.addHistory(actionTaken)
        Log.i("TIMBER DISCONNECT", "${connection.handle()} disconnected")
        //build intent
        val intent = Intent()
        intent.setClassName(context, activityClass)
        intent.putExtra("handle", clientHandle)
    }

    /**
     * A connection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private fun connect() {
        val connection = getInstance(context).getConnection(clientHandle)
        connection!!.changeConnectionStatus(Connection.ConnectionStatus.CONNECTED)
        connection.addHistory("Client Connected")
        Log.i("COnnect","${connection.handle()} connected.")
        val subscriptions = this.connection.getSubscriptions()
        for (sub in subscriptions) {
            Log.i("Auto-subscribing to:", "${sub.topic} @ QoS: ${sub.qos}")
            this.connection.client.subscribe(sub.topic, sub.qos.value)
        }
    }

    /**
     * The action associated with the object was a failure
     *
     * @param token     This argument is not used
     * @param exception The exception which indicates why the action failed
     */
    override fun onFailure(token: IMqttToken?, exception: Throwable?) {
        Log.e("timber Onfalure", "$exception token=$token")
        exception?.let {
            when (action) {
                Action.CONNECT -> connect(it)
                Action.DISCONNECT -> disconnect(it)
                Action.SUBSCRIBE -> subscribe(it)
                Action.PUBLISH -> publishNotify(it)
            }
        }
    }

    /**
     * A publish action was unsuccessful, notify user and update client history
     */
    private fun publishNotify(exception: Throwable) {
        val connection = getInstance(context).getConnection(clientHandle)

        @SuppressLint("StringFormatMatches")
        val action = "Publish notify" + " " + exception.message
        connection!!.addHistory(action)
        toast(context, action, Toast.LENGTH_SHORT)
        Log.e("Timber","Publish failed")
    }

    /**
     * A addNewSubscription action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private fun subscribe(exception: Throwable) {
        val connection = getInstance(context).getConnection(clientHandle)
        val action = "subscribe" + " " + exception.message
        connection!!.addHistory(action)
        toast(context, action, Toast.LENGTH_SHORT)
        Log.e("TIMBER",action)
    }

    /**
     * A disconnect action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private fun disconnect(exception: Throwable) {
        val connection = getInstance(context).getConnection(clientHandle)
        connection!!.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED)
        connection.addHistory("Disconnect Failed - an error occurred ${exception.message}")
    }

    /**
     * A connect action was unsuccessful, notify the user and update client history
     *
     * @param exception This argument is not used
     */
    private fun connect(exception: Throwable) {
        val connection = getInstance(context).getConnection(clientHandle)
        connection!!.changeConnectionStatus(Connection.ConnectionStatus.ERROR)
        connection.addHistory("Client failed to connect")
        Log.e("TIMBER","Client failed to connect ${exception.message}")
    }

    companion object {
        private var activityClass = MainActivity::class.java.name
    }

}