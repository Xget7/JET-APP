package xget.dev.jet.data.remote.users

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import xget.dev.jet.data.remote.HttpRoutes
import xget.dev.jet.data.remote.HttpRoutes.REGISTER_USER
import xget.dev.jet.data.remote.ApiResponse
import xget.dev.jet.data.remote.users.dto.UserRequest
import xget.dev.jet.data.remote.users.dto.UserResponse
import javax.inject.Inject

class UserServiceImpl @Inject constructor(
    private val client: HttpClient
) : UserService {


    override suspend fun getUser(uid: String): ApiResponse<UserResponse>? {
        return try {
            val response = client.get { url(HttpRoutes.GET_USER) }.body<UserResponse>()
            ApiResponse.Success(response)
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            ApiResponse.Error("Error: ${e.response.status.description}")
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            ApiResponse.Error("Error: ${e.response.status.description}")
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            ApiResponse.Error("Error: ${e.response.status.description}")
        } catch(e: Exception) {
            println("Error: ${e.message}")
            ApiResponse.Error("Error: ${e.localizedMessage}")
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun registerUser(userRequest: UserRequest): ApiResponse<UserResponse>? {
        return try {
            val response : UserResponse = client.post(REGISTER_USER){
                contentType(ContentType.Application.Json)
                body = userRequest
            }.body()
            ApiResponse.Success(response)
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            ApiResponse.Error("Error: ${e.response.status.description}")
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            ApiResponse.Error("Error: ${e.response.status.description}")
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            ApiResponse.Error("Error: ${e.response.status.description}")
        } catch(e: Exception) {
            println("Error: ${e.message}")
            ApiResponse.Error("Error: ${e.localizedMessage}")
        }
    }


}