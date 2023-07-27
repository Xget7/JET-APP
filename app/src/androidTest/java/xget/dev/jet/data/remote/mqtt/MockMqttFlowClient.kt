package xget.dev.jet.data.remote.mqtt

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import xget.dev.jet.domain.model.mqtt.ReceivedMessage
import xget.dev.jet.domain.services.mqtt.MqttFlowClient

class MockMqttFlowClient(

) : MqttFlowClient {


    private val _connectionStatus = MutableStateFlow(false)
    override val connectionStatus: SharedFlow<Boolean> get() = _connectionStatus.asSharedFlow()
    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    override fun startMqttService() {
        _connectionStatus.value = true
    }

    private fun mockDeviceState(state: Int, online: Int): String {
        return """
           {
                "state": $state,
                "online": $online
           }
        """
    }

    override fun subscribeAndListen(topic: String): Flow<ReceivedMessage> {
        return flow {
            delay(1000) // Simulate some delay
            emit(ReceivedMessage(topic = topic, message = mockDeviceState(state = 0, online = 1)))
            delay(1000)
            emit(ReceivedMessage(topic = topic, message = mockDeviceState(state = 1, online = 1)))
            delay(1000)
            emit(ReceivedMessage(topic = topic, message = mockDeviceState(state = 1, online = 1)))
            delay(1000)
            emit(ReceivedMessage(topic = topic, message = mockDeviceState(state = 1, online =0)))
        }
    }

    override suspend fun unSubscribe(topic: String): Boolean {
        return true
    }

    override suspend fun publish(topic: String, data: String): Boolean {
        delay(500) // Simulate some delay
        return true
    }

    override fun disconnectFromClient() {
        _connectionStatus.value = false
    }

    override fun release() {

    }
}