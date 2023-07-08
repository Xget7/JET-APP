package xget.dev.jet.data.remote.devices.mqtt

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import xget.dev.jet.domain.model.mqtt.ReceivedMessage
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.repository.token.Token
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

/**
 * Service implementation for managing MQTT-based communication with smart devices.
 * Handles subscriptions, message parsing, and publishing messages to devices.
 *
 * @param mqttClient The MQTT client responsible for handling the MQTT communication.
 * @param token The token for user authentication.
 */
class DevicesMqttServiceImpl @Inject constructor(
    private val mqttClient: MqttFlowClient,
    val token: Token
) : DevicesMqttService {

    // Flow to emit any errors that occur during MQTT communication
    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    // Gson instance for parsing MQTT messages
    private val gson = Gson()


    // Number of retries for subscriptions
    private var subscriptionsRetry = 0

    // Flow for emitting states of multiple devices
    private val devicesStatesFlow =
        MutableStateFlow<List<Pair<String, SmartDeviceMqtt>>>(emptyList())

    // Flow for emitting the state of a single device
    private val oneDeviceStateFlow =
        MutableStateFlow(Pair("", SmartDeviceMqtt(0, 0)))

    /**
     * Initializes the MQTT service and establishes the connection.
     *
     * @return A flow that emits true if the connection is successful, false otherwise.
     */
    override fun init(): Flow<Boolean> {
        mqttClient.startMqttService()
        return mqttClient.connectionStatus
    }

    /**
     * Collects any MQTT errors that occur during communication.
     * Updates the [_errors] flow with the error message.
     */
    override suspend fun collectMqttErrors() {
        mqttClient.errors.collect { mqttError ->
            _errors.update {
                mqttError
            }
            Log.d("ERROR COLLECTOR", mqttError)
        }
    }

    /**
     * Subscribes to MQTT topics for multiple devices and collects their states.
     *
     * @param devicesId The list of device IDs to subscribe to.
     * @return A flow that emits the states of the subscribed devices.
     */
    override fun subscribeAndCollectDevices(
        devicesId: List<String>,
        userId: String
    ): Flow<List<Pair<String, SmartDeviceMqtt>>> = flow {
        val subscribeTopic = "$userId/#"

        try {
            emit(devicesStatesFlow.value)

            mqttClient.subscribeAndListen(subscribeTopic).collect { msg ->
                for (deviceId in devicesId) {
                    Log.d(
                        "topic problems",
                        "topic arrived: ${msg.topic} , current topic ${"$userId/$deviceId/data"}"
                    )
                    when (msg.topic) {
                        "$userId/$deviceId/data" -> {
                            Log.d(
                                "topic problems",
                                "topic arrived and filteredddd : ${msg.topic}}"
                            )
                            parseDevicesState(msg, deviceId)
                            emit(devicesStatesFlow.value)
                        }
                    }
                }
            }


        } catch (e: Exception) {
            _errors.update {
                "Error conectando con los dispositivos."
            }
        }

    }.flowOn(Dispatchers.IO)

    /**
     * Subscribes to MQTT topics for a single device and collects its state.
     *
     * @param deviceId The ID of the device to subscribe to.
     * @return A flow that emits the state of the subscribed device.
     */
    override fun subscribeAndCollectOneDevice(
        deviceId: String,
        userId: String
    ): Flow<Pair<String, SmartDeviceMqtt>> =
        flow {
            val subscribeTopic = "$userId/$deviceId/#"
            try {
                emit(oneDeviceStateFlow.value)
                mqttClient.subscribeAndListen(subscribeTopic).collect { msg ->
                    when (msg.topic) {
                        "$userId/$deviceId/data" -> {
                            parseOneDeviceState(msg, deviceId)
                            emit(oneDeviceStateFlow.value)
                        }
                    }
                }
            } catch (e: Exception) {
                _errors.update {
                    "Erorr conectando al dispositivo."
                }
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Parses the received MQTT message for multiple devices and updates their states.
     *
     * @param msg The received MQTT message.
     * @param deviceId The ID of the device.
     */
    private fun parseDevicesState(msg: ReceivedMessage, deviceId: String) {
        if (msg.message.contains("online")) {
            val mqttState = gson.fromJson(msg.message, SmartDeviceMqtt::class.java)
            val updatedDevice = SmartDeviceMqtt(mqttState.online, mqttState.state)
            val mutableListState = devicesStatesFlow.value.toMutableList()
            if (devicesStatesFlow.value.firstOrNull { it.first == deviceId } == null) {
                mutableListState.add(Pair(deviceId, updatedDevice))
            } else {
                val temp = mutableListState.indexOfFirst { it.first == deviceId }
                mutableListState[temp] = Pair(deviceId, updatedDevice)
            }
            Log.d("updatedDevicesStateFLow", "received message to update ${msg.message}")
            devicesStatesFlow.update {
                mutableListState.toList()
            }
        }
    }

    /**
     * Parses the received MQTT message for a single device and updates its state.
     *
     * @param msg The received MQTT message.
     * @param deviceId The ID of the device.
     */
    private fun parseOneDeviceState(msg: ReceivedMessage, deviceId: String) {
        if (msg.message.contains("online")) {
            val mqttState = gson.fromJson(msg.message, SmartDeviceMqtt::class.java)
            val updatedDevice = SmartDeviceMqtt(mqttState.online, mqttState.state)

            oneDeviceStateFlow.update {
                Pair(deviceId, updatedDevice)
            }
        }
    }

    /**
     * Sends a message to a device using MQTT.
     *
     * @param deviceId The ID of the device to send the message to.
     * @param data The data to send.
     * @return True if the message was sent successfully, false otherwise.
     */
    override suspend fun sendMessageToDevice(
        deviceId: String,
        data: String,
        userId: String
    ): Boolean {
        Log.d("SendMsgToDevice", deviceId + data)
        return mqttClient.publish("$userId/$deviceId/data", data)
    }

    /**
     * Releases the MQTT service and unsubscribes from all topics.
     */
    override suspend fun release() {
        mqttClient.unSubscribe("#")
        mqttClient.release()
    }
}

