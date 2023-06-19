package xget.dev.jet.data.devices

import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xget.dev.jet.data.remote.devices.mqtt.DevicesMqttServiceImpl
import xget.dev.jet.data.remote.mqtt.MqttFlowClientImpl
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

@HiltAndroidTest
class DevicesMqttServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mqttFlowClient : MqttFlowClient

    @Before
    fun init(){
        hiltRule.inject()
    }



    @Test
    fun testMqttFlowClientImpl() {
        // Perform your test assertions or actions on the `mqttFlowClient` object
        // For example, you can call methods and check the returned values or verify the behavior

        // Example assertion:
        assertTrue(mqttFlowClient.connectionStatus.value)
    }


    @Test
    fun testStartMqttService_SuccessfulConnection() {

        mqttFlowClient.startMqttService()

        assertTrue(mqttFlowClient.connectionStatus.value)
        assertEquals("", mqttFlowClient.errors.replayCache.firstOrNull())
    }

    @Test
    fun testStartMqttService_FailedConnection() {

        // Set up a mock server or modify the MQTT broker address to simulate a failed connection
        // ...

        mqttFlowClient.startMqttService()

        assertFalse(mqttFlowClient.connectionStatus.value)
        assertNotNull(mqttFlowClient.errors.replayCache.firstOrNull())
    }

    @Test
    fun testSubscribe_SuccessfulSubscription() = runBlocking{
        val topic = "test/topic"
        val flow = mqttFlowClient.subscribe(topic)

        val collector = flow.last()

        // Simulate a failed subscription
        // ...
        assertEquals(collector,true)
    }

    @Test
    fun testSubscribe_FailedSubscription()  = runBlocking(){
        val topic = "test/topic"
        val flow = mqttFlowClient.subscribe(topic)

        val collector = flow.last()

        // Simulate a failed subscription
        // ...
        assertFalse(collector)
        assertNotNull(mqttFlowClient.errors.replayCache.firstOrNull())
    }

    @Test
    fun testUnsubscribe_SuccessfulUnsubscription()  = runBlocking(){

        val topic = "test/topic"
        val flow = mqttFlowClient.unSubscribe(topic)

        val collector = flow.last()

        // Simulate a successful unsubscription
        // ...

        assertEquals(collector,true)
    }

    @Test
    fun testUnsubscribe_FailedUnsubscription() = runBlocking{


        val topic = "test/topic"
        val flow = mqttFlowClient.unSubscribe(topic)

        val collector = flow.last()

        // Simulate a failed unsubscription
        // ...

        assertFalse(collector)
        assertNotNull(mqttFlowClient.errors.replayCache.firstOrNull())
    }

    @Test
    fun testReceiveMessages_MessageArrived()= runBlocking {

        val flow = mqttFlowClient.receiveMessages()

        val collector = flow.last()

        // Simulate a message arrival
        // ...

        assertNotNull(collector.message.isNotBlank())
    }

    @Test
    fun testPublish_MessagePublished() {

        val topic = "test/topic"
        val data = "Hello, MQTT!"

        mqttFlowClient.publish(topic, data)

        // Assert the publish action
        // ...
    }

    @Test
    fun testDisconnectFromClient_SuccessfulDisconnect() {

        mqttFlowClient.disconnectFromClient()

        assertFalse(mqttFlowClient.connectionStatus.value)
        assertEquals("", mqttFlowClient.errors.replayCache.firstOrNull())
    }




}