package xget.dev.jet.data.remote.mqtt

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback.*
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos
import com.amazonaws.regions.Regions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import xget.dev.jet.data.util.network.WifiStatusReceiver
import xget.dev.jet.domain.model.mqtt.ReceivedMessage
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import java.io.UnsupportedEncodingException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MqttFlowClientImpl @Inject constructor(
    private val context: Context,
    private val awsIotMqttClient: AWSIotMqttManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    MqttFlowClient {

    private val REGION = Regions.US_EAST_2
    private val Qos = AWSIotMqttQos.QOS0

    lateinit var credentialsProvider: CognitoCachingCredentialsProvider

    private val _connectionStatus = MutableStateFlow(false)
    override val connectionStatus: SharedFlow<Boolean> get() = _connectionStatus.asSharedFlow()
    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()


    var connectionsRetry = 0

    private val wifiStatusReceiver = WifiStatusReceiver { connection ->
        CoroutineScope(dispatcher).launch {
            delay(1000)
            if (!_connectionStatus.value && connection) {
                startMqttService()
            } else {
                connectionsRetry++
            }
        }
    }

    init {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)
        context.registerReceiver(wifiStatusReceiver, intentFilter)
    }



    override fun startMqttService() {
        credentialsProvider = CognitoCachingCredentialsProvider(
            context,  // context
            xget.dev.jet.BuildConfig.COGNITO_POOL_ID,  // Identity Pool ID
            REGION // Region
        )
        try {
            awsIotMqttClient.connect(
                credentialsProvider
            ) { status, throwable ->
                Log.d("LOG_TAG", "Status = $status")
                when (status) {
                    AWSIotMqttClientStatus.Connecting -> {
                    }
                    AWSIotMqttClientStatus.Connected -> {
                        _connectionStatus.update {
                            true
                        }
                    }
                    AWSIotMqttClientStatus.Reconnecting -> {
                        throwable?.let {
                            Log.e("LOG_TAG", "Connection error.", it)
                        }
                        if (connectionsRetry > 3) {
                            _errors.update {
                                "Error intentato reconectarse."
                            }
                        }

                    }

                    AWSIotMqttClientStatus.ConnectionLost -> {
                        throwable?.let {
                            Log.e("LOG_TAG", "Connection error.", it)
                        }
                        _connectionStatus.update {
                            false
                        }
                    }

                    else -> {
                        _connectionStatus.update {
                            false
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _connectionStatus.update {
                false
            }
            _errors.update {
                "Error intentato conectarse al servidor."
            }
        }

    }

    override fun subscribeAndListen(topic: String): Flow<ReceivedMessage> = callbackFlow {
        try {
            Log.i("Try subscribing", "subscribed to ${topic} success ")

            awsIotMqttClient.subscribeToTopic(
                topic, Qos
            ) { topic, data ->
                val msg = String(data, charset("UTF-8"))
                Log.d("listening", "from aws $msg")
                val receivedMessage = ReceivedMessage(
                    connection = true,
                    message = msg,
                    topic = topic,
                    error = null
                )
                trySend(receivedMessage)
            }

        } catch (e: Exception) {
            val receivedMessage = ReceivedMessage(
                connection = false,
                topic = topic,
                error = "Error al recibir estado del dispositivo."
            )
            trySend(receivedMessage)

        }
        awaitClose { }
    }.flowOn(dispatcher)



    override suspend fun unSubscribe(topic: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            try {
                awsIotMqttClient.unsubscribeTopic(topic)
            } catch (e: MqttException) {
                // Callback de fallo en la desuscripción
                continuation.resume(false)
            }

            continuation.invokeOnCancellation {
                Log.d("MQTT", "EXCEPTION cancelattion", it)
            }
        }

    override suspend fun publish(topic: String, data: String) = suspendCancellableCoroutine {
        try {
            awsIotMqttClient.publishString(data, topic, Qos).apply {
                it.resume(true)
            }
        } catch (e: Exception) {
            it.resume(false)

            _errors.update {
                "Error al enviar accion al dispositivo"
            }
        }
    }

    override fun disconnectFromClient() {
        try {
            val disconnectState = awsIotMqttClient.disconnect()
            if (!disconnectState) {
                _errors.update {
                    "Error intentando desconectarse del servidor."
                }
            }
        } catch (e: Exception) {
            // Give Callback on error here
            _errors.update {
                "Error intentando desconectarse del servidor."
            }
        }
    }


    override fun release() {
        context.unregisterReceiver(wifiStatusReceiver)
        disconnectFromClient()
    }


}