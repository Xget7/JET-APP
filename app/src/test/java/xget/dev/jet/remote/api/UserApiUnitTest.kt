package xget.dev.jet.remote.api

import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import xget.dev.jet.data.remote.users.UserService
import xget.dev.jet.data.remote.users.UserServiceImpl
import xget.dev.jet.data.remote.users.dto.RegisterRequest
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.remote.api.creators.ktorErrorClient
import xget.dev.jet.remote.api.creators.ktorSuccessClient
import xget.dev.jet.remote.util.LogMockExtension
import xget.dev.jet.core.utils.MockToken


@ExtendWith(LogMockExtension::class)
internal class UserApiUnitTest {

    lateinit var userApiService : UserService
    val mockToken = MockToken()


    @Nested
    @DisplayName("UserAPi Success Responses")
    internal inner class ReturnSuccess {

        init {
            userApiService = spyk<UserService>(UserServiceImpl(ktorSuccessClient,mockToken))
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        @DisplayName("Create User")
        fun `Register user with success response`(): Unit = runTest {

            val mockRequest =  RegisterRequest(
                "xget",
                "xget@gmail.com",
                "hashedPasword",
                "profilepic",
                "112342"
            )

            val result = userApiService.registerUser(
               mockRequest
            )
            println(result?.errorMsg)
            assertInstanceOf(ApiResponse.Success::class.java, result)
            Assertions.assertEquals("839438949839848398d3894893d489",(result as ApiResponse.Success).data?.id)
        }


        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        @DisplayName("Fetch User")
        fun `Fetch user with success response`(): Unit = runTest {
            val result = userApiService.getUser()
            println(result?.errorMsg)
            val resultData = (result as ApiResponse.Success).data


            assertInstanceOf(ApiResponse.Success::class.java, result)
            Assertions.assertEquals("example_username",resultData?.userName)
            Assertions.assertEquals("123e4567-e89b-12d3-a456-426655440000",resultData?.id)
        }
    }

    @Nested
    @DisplayName("UserAPi Error Responses")
    internal inner class ReturnError{

        init {
            userApiService = spyk<UserService>(UserServiceImpl(ktorErrorClient, mockToken))
        }
        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        @DisplayName("Create User")
        fun `Register user with error response`(): Unit = runTest {

            val mockRequest =  RegisterRequest(
                "xget",
                "xget@gmail.com",
                "hashedPasword",
                "profilepic",
                "112342"
            )

            val result = userApiService.registerUser(
                mockRequest
            )
            assertInstanceOf(ApiResponse.Error::class.java, result)
            val resultData = (result as ApiResponse.Error).errorMsg
            Assertions.assertNotNull(resultData)
        }


        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        @DisplayName("Fetch User")
        fun `Fetch user with error response`(): Unit = runTest {

            val result = userApiService.getUser()
            println(result?.errorMsg)

            assertInstanceOf(ApiResponse.Error::class.java, result)

            val resultData = (result as ApiResponse.Error).errorMsg
            Assertions.assertNotNull(resultData)
        }
    }
}