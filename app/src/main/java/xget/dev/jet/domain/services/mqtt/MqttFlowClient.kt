package xget.dev.jet.domain.services.mqtt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import xget.dev.jet.domain.model.mqtt.ReceivedMessage

interface MqttFlowClient {


    val connectionStatus: StateFlow<Boolean>
    val errors: SharedFlow<String>

    fun startMqttService()

    suspend fun subscribe(topic: String): Boolean
    fun unSubscribe(topic: String): Flow<Boolean>
    fun receiveMessages(): Flow<ReceivedMessage>

    fun publish(topic: String, data: String)
    fun disconnectFromClient()
}