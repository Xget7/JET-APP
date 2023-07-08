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

        assertNotNull(mqttFlowClient.errors.replayCache.firstOrNull())
    }

    @Test
    fun testSubscribe_SuccessfulSubscription() = runTest{
        val topic = "test/topic"

    }




}