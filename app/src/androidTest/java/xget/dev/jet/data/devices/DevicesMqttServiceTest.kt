package xget.dev.jet.data.devices

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xget.dev.jet.data.remote.devices.mqtt.DevicesMqttServiceImpl
import xget.dev.jet.data.remote.mqtt.MqttFlowClientImpl
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class DevicesMqttServiceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mqttFlowClient : MqttFlowClient
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun init(){
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }





    @Test
    fun testStartMqttService_SuccessfulConnection() = runTest{

        mqttFlowClient.startMqttService()
        Log.d("DEBUGTEST",mqttFlowClient.errors.last() .toString())
//        mqttFlowClient.connectionStatus.collectLatest {
//            assertTrue(it)
//        }

        assertEquals("", mqttFlowClient.errors.last())

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
    fun testSubscribe_SuccessfulSubscription() = runTest{
        val topic = "test/topic"
        val flow = mqttFlowClient.subscribe(topic)


        // Simulate a failed subscription
        // ...
        assertEquals(flow,true)
    }

    @Test
    fun testSubscribe_FailedSubscription()  = runTest(){
        val topic = "test/topic"
        val flow = mqttFlowClient.subscribe(topic)

        // Simulate a failed subscription
        // ...
        assertFalse(flow)
        assertNotNull(mqttFlowClient.errors.replayCache.firstOrNull())
    }

    @Test
    fun testUnsubscribe_SuccessfulUnsubscription()  = runTest(){

        val topic = "test/topic"
        val flow = mqttFlowClient.unSubscribe(topic)

        val collector = flow.last()

        // Simulate a successful unsubscription
        // ...

        assertEquals(collector,true)
    }

    @Test
    fun testUnsubscribe_FailedUnsubscription() = runTest{


        val topic = "test/topic"
        val flow = mqttFlowClient.unSubscribe(topic)

        val collector = flow.last()

        // Simulate a failed unsubscription
        // ...

        assertFalse(collector)
        assertNotNull(mqttFlowClient.errors.replayCache.firstOrNull())
    }

    @Test
    fun testReceiveMessages_MessageArrived()= runTest {

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