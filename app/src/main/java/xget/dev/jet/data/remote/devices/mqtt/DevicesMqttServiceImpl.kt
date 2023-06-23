package xget.dev.jet.data.remote.devices.mqtt

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import xget.dev.jet.domain.model.mqtt.ReceivedMessage
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.repository.token.Token
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

class DevicesMqttServiceImpl @Inject constructor(
    private val mqttClient: MqttFlowClient,
    val token: Token
) : DevicesMqttService {

    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()


    private val gson = Gson()
    var userId: String = if (token.getUserIdLocal() == null) {
        _errors.update {
            "Error obteniendo usuario"
        }
        ""
    } else {
        token.getUserIdLocal()!!
    }
    private var subscriptionsRetry = 0


    private val devicesStatesFlow = MutableStateFlow<List<SmartDeviceMqtt>>(emptyList())

    init {
        mqttClient.startMqttService()
    }

    override suspend fun collectMqttErrors() {
        mqttClient.errors.collect { mqttErorr ->
            _errors.update {
                mqttErorr
            }
            Log.d("ERROR CONLLECTOR", mqttErorr)
        }
    }

    override fun subscribeAndCollectDevices(
        devicesId: List<String>
    ): Flow<List<SmartDeviceMqtt>> = flow {

        var subscriptionState =  mqttClient.subscribe("e134b077-d0db-48a5-ab5a-0512c460559a/7821849C1782")
        Log.d("subscribedSucess", subscriptionState.toString())

        emit(listOf<SmartDeviceMqtt>())

//        while (!subscriptionState && subscriptionsRetry <= 4) {
//            subscriptionsRetry++
//            subscriptionState = mqttClient.subscribe("$userId/#").last()
//        }

//        if (subscriptionState) {
//            Log.d("receivingMessages", "susb correct")
//            mqttClient.receiveMessages().collect { msg ->
//                Log.d("incomingMessage", msg.message)
//                for (deviceId in devicesId) {
//                    when (msg.topic) {
//                        //TOOD (harcoded ID)
//                        "e134b077-d0db-48a5-ab5a-0512c460559a/$deviceId/data" -> {
//                            // Process the message and determine the device state
//                            // Add the device state to the list
//
//                            parseDevicesState(msg)
//                            emit(devicesStatesFlow.value)
//                        }
//                    }
//                }
//            }
//        } else {
//            _errors.update {
//                "Error en las conexiones con dispositivos."
//            }
//        }
    }.flowOn(Dispatchers.Default)


    private fun parseDevicesState(msg: ReceivedMessage) {
        devicesStatesFlow.value.forEachIndexed { index, device ->
            if (msg.message.contains("online")) {
                val mqttState = gson.fromJson(msg.message, SmartDeviceMqtt::class.java)
                val updatedDevice = device.copy(
                    online = mqttState.online,
                    state = mqttState.state
                )
                val mutable = devicesStatesFlow.value.toMutableList()
                mutable[index] = updatedDevice
                devicesStatesFlow.update {
                    mutable.toList()
                }
            }
        }
    }


    override suspend fun sendMessageToDevice(deviceId: String, data: String): Boolean {
        return mqttClient.publish("$userId/$deviceId/data", data)
    }

    override suspend fun release() {
        mqttClient.unSubscribe("$userId/#")
        mqttClient.release()
    }
}

