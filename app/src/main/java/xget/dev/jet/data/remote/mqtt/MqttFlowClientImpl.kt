package xget.dev.jet.data.remote.mqtt

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.content.ContextCompat.registerReceiver
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import xget.dev.jet.data.util.network.WifiStatusReceiver
import xget.dev.jet.domain.model.mqtt.ReceivedMessage
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MqttFlowClientImpl @Inject constructor(
    private val context: Context,
    private val mqttAndroidClient: MqttAndroidClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    MqttFlowClient {


    private val _connectionStatus = MutableStateFlow(false)
    override val connectionStatus: StateFlow<Boolean> get() = _connectionStatus.asStateFlow()

    var connectionsRetry = 0

    private val wifiStatusReceiver = WifiStatusReceiver {connection ->
        CoroutineScope(dispatcher).launch {
            delay(3000)
            if (!_connectionStatus.value && connection){
                Log.d("LostConnection , update?", connection.toString())
                startMqttService()
            }else{
                connectionsRetry++
            }
        }
    }

    init {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)
        context.registerReceiver(wifiStatusReceiver, intentFilter)
    }

    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    override fun startMqttService() {
        val connOpts = MqttConnectOptions()
        connOpts.userName = "xget"
        connOpts.password = "eltupa2005".toCharArray()
        try {
            val token = mqttAndroidClient.connect(connOpts)
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i("Connection", "success ")
                    _connectionStatus.update {
                        true
                    }
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.i("Connection", "failure")
                    _connectionStatus.update {
                        false
                    }
                    if (connectionsRetry > 3){
                        _errors.update {
                            "Posiblemente no haya conexion a internet. Revisa tu wifi."
                        }
                    }
                    exception.printStackTrace()
                }
            }
        } catch (e: MqttException) {
            Log.d("MQTTEXCEPTION", e.localizedMessage, e)
            handleMqttException(e)
        }
    }

    override suspend fun subscribe(topic: String)  : Boolean = suspendCoroutine { continuation ->
            val qos = 1 // Mention your qos value
            try {
                Log.i("Try subscribing", "success ")


                mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        // Give your callback on Subscription here
                        Log.d("MQTT", "subscribed to $topic, ${asyncActionToken.topics}")
                        continuation.resumeWith(Result.success(true))
                    }

                    override fun onFailure(
                        asyncActionToken: IMqttToken,
                        exception: Throwable
                    ) {
                        // Give your subscription failure callback here
                        Log.d("MQTT", "failure to $topic, ${asyncActionToken.topics}")
                        _errors.update {
                            "Error al intentar conectar al dispositivo."
                        }
                        continuation.resumeWith(Result.success(false))

                    }
                })

            } catch (e: MqttException) {
                // Give your subscription failure callback here
                continuation.resumeWith(Result.failure(Throwable("Error de conexión al intentar escuchar al dispositivo.")))
            }

        }


    override suspend fun unSubscribe(topic: String): Boolean = suspendCancellableCoroutine { continuation ->
        try {
            val unsubToken = mqttAndroidClient.unsubscribe(topic)
            unsubToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Callback de desuscripción exitosa
                    continuation.resume(true)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Callback de fallo en la desuscripción
                    continuation.resume(false)
                    _errors.update {
                        "Error dejar de escuchar al dispositivo."
                    }
                }
            }
        } catch (e: MqttException) {
            // Callback de fallo en la desuscripción
            continuation.resume(false)
            handleMqttException(e)
        }

        continuation.invokeOnCancellation {
            Log.d("MQTT", "EXCEPTION cancelattion", it)
        }
    }

    override fun receiveMessages(): Flow<ReceivedMessage> = callbackFlow {
        Log.d("receivedMessageSet", "setted")
        val _receivedMEssage = MutableStateFlow(ReceivedMessage())
        val receivedMessage = _receivedMEssage.asStateFlow()


        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                Log.d("receivedMessageState", "connectionLost", cause)

                _receivedMEssage.update { it.copy(connection = false) }

                trySend(
                    receivedMessage.value
                )
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                try {
                    Log.d("receivedMessageState", "MESSAGE $message")

                    val data = String(message.payload, charset("UTF-8"))
                    _receivedMEssage.update {
                        it.copy(
                            connection = true,
                            message = data,
                            topic = topic,
                            error = null
                        )
                    }

                    trySend(
                        receivedMessage.value
                    )
                } catch (e: Exception) {
                    Log.d("receivedMessageState", "CATCH EXECTION MESSAGE $message", e)

                    _receivedMEssage.update {
                        it.copy(
                            connection = false,
                            topic = topic,
                            error = "Error al recibir estado del dispositivo."
                        )
                    }
                    trySend(
                        receivedMessage.value
                    )
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {}
        })

        awaitClose { }
    }.flowOn(dispatcher)

    override suspend fun publish(topic: String, data: String) =  suspendCancellableCoroutine {
        val encodedPayload: ByteArray
        try {
            encodedPayload = data.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            message.qos = 1
            message.isRetained = false
            val response = mqttAndroidClient.publish(topic, message)
            response.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    it.resume(true)
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Callback de fallo en la desuscripción
                    it.resume(false)
                    _errors.update {
                        "Error al enviar accion al dispositivo."
                    }
                }
            }
          
        } catch (e: Exception) {
            _errors.update {
                "Error al enviar accion al dispositivo"
            }

        } catch (e: MqttException) {
            handleMqttException(e)
        }

    }

    override fun disconnectFromClient() {
        try {
            val disconToken = mqttAndroidClient.disconnect()
            disconToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    _connectionStatus.update {
                        false
                    }
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    _errors.update {
                        "Error intentando desconectarse del servidor."
                    }
                }
            }
        } catch (e: MqttException) {
            // Give Callback on error here
            handleMqttException(e)
        }
    }


    private fun handleMqttException(exception: MqttException) {
        // Log the exception details
        Log.d("MQTTEXCEPTION", exception.localizedMessage, exception)

        // Handle the exception based on the reason code
        when (exception.reasonCode) {
            MqttException.REASON_CODE_CLIENT_EXCEPTION.toInt() -> {
                // Handle client exception
                _errors.update {
                    "Error en el servidor."
                }
            }

            MqttException.REASON_CODE_INVALID_PROTOCOL_VERSION.toInt() -> {
                // Handle invalid protocol version
                _errors.update {
                    "Versión de protocolo MQTT no válida."
                }
            }

            MqttException.REASON_CODE_INVALID_CLIENT_ID.toInt() -> {
                // Handle invalid client ID
                _errors.update {
                    "ID de cliente MQTT no válido."
                }
            }
            // Handle other reason codes as needed
            else -> {
                // Handle generic MqttException
                _errors.update {
                    "Error en la conexión MQTT."
                }
            }
        }
    }

    override fun release() {
        context.unregisterReceiver(wifiStatusReceiver)
        disconnectFromClient()
    }


}