package xget.dev.jet.remote.api

import android.util.Log
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import xget.dev.jet.data.remote.devices.rest.DevicesRemoteServiceImpl
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.remote.devices.rest.dto.history.DeviceActionReq
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.presentation.utils.DevicesTypeObj
import xget.dev.jet.remote.api.creators.badRequestJSON
import xget.dev.jet.remote.api.creators.ktorErrorClient
import xget.dev.jet.remote.api.creators.ktorSuccessClient
import xget.dev.jet.remote.util.LogMockExtension
import xget.dev.jet.remote.util.MockToken


@ExtendWith(LogMockExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class DevicesAPiUnitTest {


    lateinit var devicesService: DevicesRemoteService
    val mockToken = MockToken()

    @AfterEach
    fun cleanup() {
        // Clear static mocks after each test
        unmockkStatic(Log::class)
    }

    @Nested
    @DisplayName("DevicesApi Success Responses")
    internal inner class ReturnSuccess {
        @BeforeEach
        fun setup() {
            devicesService = DevicesRemoteServiceImpl(ktorSuccessClient, mockToken)
        }

        @Test
        @DisplayName("Fetch Devices By User")
        fun `Fetch Devices by User and check name and id`() = runTest {
            // Set up the response for getDevicesByUser call is made in KtorSuccessClient

            // Observe the flow from the function being tested
            val flowResult = devicesService.getDevicesByUser().toList()

            // Assertions
            val result = flowResult.last()

            Assertions.assertInstanceOf(ApiResponse.Success::class.java, result)

            val resultData = (result as ApiResponse.Success).data

            Assertions.assertNotNull(resultData)
            Assertions.assertTrue(resultData?.myDevices!!.isNotEmpty())
            Assertions.assertTrue(resultData.myDevices.any { it.name.contains("heladera") })
            Assertions.assertTrue(resultData.myDevices.any { it.id.contains("241879") })
        }

        @Test
        @DisplayName("Get Device History")
        fun `Fetch device history with success response`() = runTest {

            // Call the function being tested
            val result = devicesService.getDeviceHistory("deviceId")


            // Assertions
            Assertions.assertInstanceOf(ApiResponse.Success::class.java, result)

            val resultData = result.data

            Assertions.assertNotNull(resultData)
            Assertions.assertFalse(resultData?.history.isNullOrEmpty())
            print(result.data?.history)
        }


        @Test
        @DisplayName("Create Device")
        fun `Create device with success response`() = runTest {
            // Set up the response for createDevice call
            val mockDevice = DeviceDto(
                "8fv9d8vdfvdf89",
                "Fridge",
                userId = "e82398f9ds8",
                deviceType = DevicesTypeObj.DevicesType.GATE.name
            )
            // Observe the flow from the function being tested
            val resultFlow = devicesService.createDevice(mockDevice)


            // Assertions
            val result = resultFlow.toList()[0]

            Assertions.assertInstanceOf(ApiResponse.Success::class.java, result)


            Assertions.assertNull(result.errorMsg)
            Assertions.assertNotNull(result.data)
            Assertions.assertTrue(result.data ?: false)
        }

        @Test
        @DisplayName("Add Device Action")
        fun `Add device action to history with success response`() = runTest {
            val mockDevice = DeviceActionReq(true, "389f8ds9f8", "8f984r93")
            // Observe the flow from the function being tested
            val result = devicesService.uploadDeviceAction(mockDevice)

            print(result.errorMsg)
            // Assertions
            Assertions.assertInstanceOf(ApiResponse.Success::class.java, result)
            Assertions.assertNull(result.errorMsg)
            Assertions.assertNotNull(result.data)
            Assertions.assertTrue(result.data ?: false)
        }

    }

    @Nested
    @DisplayName("DevicesApi Error Responses")
    internal inner class ReturnError {
        init {
            devicesService = DevicesRemoteServiceImpl(ktorErrorClient, mockToken)
        }

        @Test
        @DisplayName("Fetch Devices By User with Error")
        fun `Fetch Devices by User and handle error response`() = runTest {
            // Set up the response for getDevicesByUser call with an error

            // Call the function being tested
            val result = devicesService.getDevicesByUser().toList()

            // Assertions
            val lastResult = result.last()
            Assertions.assertInstanceOf(ApiResponse.Error::class.java, lastResult)

            val errorMsg = (lastResult as ApiResponse.Error).errorMsg
            print(errorMsg)
            Assertions.assertTrue(errorMsg?.contains("no", ignoreCase = true) ?: false)
        }

        @Test
        @DisplayName("Get Device History with Error")
        fun `Fetch device history and handle error response`() = runTest {
            // Set up the response for getDeviceHistory call with an error
            val deviceId = "deviceId"

            // Call the function being tested
            val result = devicesService.getDeviceHistory(deviceId)

            // Assertions
            Assertions.assertInstanceOf(ApiResponse.Error::class.java, result)
            val errorMsg = (result as ApiResponse.Error).errorMsg
            Assertions.assertTrue(errorMsg?.contains("no", ignoreCase = true) ?: false)
        }

        @Test
        @DisplayName("Create Device with Error")
        fun `Create device and handle error response`() = runTest {
            // Set up the response for createDevice call with an error
            val mockDevice = DeviceDto(
                "8fv9d8vdfvdf89",
                "Fridge",
                userId = "e82398f9ds8",
                deviceType = DevicesTypeObj.DevicesType.GATE.name
            )

            // Observe the flow from the function being tested
            val resultFlow = devicesService.createDevice(mockDevice)

            // Assertions
            val result = resultFlow.toList()[0]
            Assertions.assertInstanceOf(ApiResponse.Error::class.java, result)

            val errorMsg = (result as ApiResponse.Error).errorMsg
            Assertions.assertTrue(errorMsg?.contains("no", ignoreCase = true) ?: false)


        }

        @Test
        @DisplayName("Add Device Action with Error")
        fun `Add device action to history and handle error response`() = runTest {
            val mockDevice = DeviceActionReq(true, "389f8ds9f8", "8f984r93")
            val expectedErrorMsg = badRequestJSON

            // Call the function being tested
            val result = devicesService.uploadDeviceAction(mockDevice)

            // Assertions
            Assertions.assertInstanceOf(ApiResponse.Error::class.java, result)

            val errorMsg = (result as ApiResponse.Error).errorMsg
            print(errorMsg)
            Assertions.assertTrue(errorMsg?.contains("error", ignoreCase = true) ?: false)


        }

    }

}