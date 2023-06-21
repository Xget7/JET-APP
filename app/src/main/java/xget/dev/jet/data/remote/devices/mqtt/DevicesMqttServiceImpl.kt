package xget.dev.jet.data.remote.devices.mqtt

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.domain.model.mqtt.ReceivedMessage
import xget.dev.jet.domain.repository.devices.mqtt.DeviceState
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

class DevicesMqttServiceImpl @Inject constructor(
    private val mqttClient: MqttFlowClient,
) : DevicesMqttService {

    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    init {
        mqttClient.startMqttService()

    }

    override suspend fun collectMqttErrors() {
        mqttClient.errors.collect { mqttErorr ->
            _errors.update {
                mqttErorr
            }
        }
    }


    override fun subscribeAndCollectDevices(userId: String, devicesId: List<String>): Flow<List<DeviceState>> = flow {
        val subscriptionState =   mqttClient.subscribe("$userId/#")
        val deviceStates = mutableListOf<DeviceState>()
        if (subscriptionState) {
            mqttClient.receiveMessages().onEach { msg ->
                for ((index,deviceId) in devicesId.withIndex()) {
                    // Process the received message based on device ID
                    val topic = msg.topic
                    val message = msg.message
                    // Perform actions based on the topic and message
                    when (topic) {
                        // Example: Check if the received message is related to the current device ID
                        "$userId/$deviceId" -> {
                            // Process the message and determine the device state
                            // Add the device state to the list
                            if (msg.error != null){
                                deviceStates[index] = DeviceState.DisconnectedOrError(message)
                            }else{
                                deviceStates[index] = DeviceState.Success(message, topic)
                            }
                        }
                    }
                }

                // Emit the device states
                emit(deviceStates)
            }
        }
    }



}