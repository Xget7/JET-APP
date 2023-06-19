package xget.dev.jet.data.remote.mqtt

import android.content.Context
import android.util.Log
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import xget.dev.jet.core.utils.ConstantsShared.MQTT_BROKER_ADDRESS
import xget.dev.jet.core.utils.ConstantsShared.MQTT_CLIENT_ID
import xget.dev.jet.domain.model.mqtt.ReceivedMessage
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import java.util.Date
import javax.inject.Inject

class MqttFlowClientImpl @Inject constructor(

    private val mqttAndroidClient: MqttAndroidClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    MqttFlowClient {


    private val _connectionStatus = MutableStateFlow(false)
    override val connectionStatus: StateFlow<Boolean> get() = _connectionStatus.asStateFlow()

    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    override fun startMqttService() {
        try {
            val token = mqttAndroidClient.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.i("Connection", "success ")
                    _connectionStatus.update {
                        true
                    }
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    //connectionStatus = false
                    Log.i("Connection", "failure")
                    _connectionStatus.update {
                        false
                    }
                    _errors.update {
                        exception.message.toString()
                    }
                    exception.printStackTrace()
                    Log.d("MQTTEXCEPTION", exception.localizedMessage, exception)
                }
            }
        } catch (e: MqttException) {
            // Give your callback on connection failure here
            Log.d("MQTTEXCEPTION", e.localizedMessage, e)
            handleMqttException(e)

        }
    }

    override fun subscribe(topic: String) = callbackFlow {
        val qos = 1 // Mention your qos value
        try {
            mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Give your callback on Subscription here
                    trySend(true)
                    close()
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Give your subscription failure callback here
                    trySend(true)
                    _errors.update {
                        "Error al escuchar a el dispositivo."
                    }
                    close()
                }
            })

        } catch (e: MqttException) {
            // Give your subscription failure callback here
            trySend(false)
            _errors.update {
                "Error de conexion al intentar escuchar a el dispositivo."
            }
            close()
        }

        awaitClose {

        }
    }.flowOn(dispatcher)

    override fun unSubscribe(topic: String): Flow<Boolean> = callbackFlow {
        try {
            val unsubToken = mqttAndroidClient.unsubscribe(topic)
            unsubToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // Callback de desuscripción exitosa
                    trySend(true)
                    close() // Cerramos el flujo después de enviar el resultado exitoso
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Callback de fallo en la desuscripción
                    trySend(false)
                    _errors.update {
                        "Error dejar de escuchar al dispositivo."
                    }
                    close() // Cerramos el flujo después de enviar el resultado de fallo
                }
            }
        } catch (e: MqttException) {
            // Callback de fallo en la desuscripción
            trySend(false)
            handleMqttException(e)

            close()
        }
        awaitClose { /* No se requiere ninguna acción al cerrar el flujo */ }
    }.flowOn(dispatcher)

    override fun receiveMessages(): Flow<ReceivedMessage> = callbackFlow {
        val _receivedMEssage = MutableStateFlow(ReceivedMessage())
        val receivedMessage = _receivedMEssage.asStateFlow()
        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                _receivedMEssage.update { it.copy(connection = false) }

                trySend(
                    receivedMessage.value
                )
            }

            override fun messageArrived(topic: String, message: MqttMessage) {
                try {
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

    override fun publish(topic: String, data: String) {
        val encodedPayload: ByteArray
        try {
            encodedPayload = data.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            message.qos = 1
            message.isRetained = false
            mqttAndroidClient.publish(topic, message)
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


}