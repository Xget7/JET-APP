package xget.dev.jet.data.remote.users

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import xget.dev.jet.data.remote.HttpRoutes
import xget.dev.jet.data.remote.HttpRoutes.REGISTER_USER
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.dto.UserRequest
import xget.dev.jet.data.remote.users.dto.UserResponse
import xget.dev.jet.data.util.network.handleApiException
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject
@OptIn(InternalAPI::class)
class UserServiceImpl @Inject constructor(
    private val client: HttpClient,
    private val token: Token
) : UserService {

    private val currentToken = token.getJwtLocal()
    override suspend fun getUser(uid: String): ApiResponse<UserResponse>? {
        ApiResponse.Loading<Any>()
        return try {
            val response = client.get {
                url(HttpRoutes.GET_USER)
                parameter("uid", uid)
                header("Authorization", "Bearer $currentToken")
            }.body<UserResponse>()
            ApiResponse.Success(response)
        } catch (e: Exception) {
            handleApiException(e)
        }
    }

    override suspend fun registerUser(userRequest: UserRequest): ApiResponse<UserResponse>? {
        Log.d("urlToPost", REGISTER_USER)
        ApiResponse.Loading<Any>()
        return try {
            val response: UserResponse = client.post(REGISTER_USER) {
                contentType(ContentType.Application.Json)
                setBody(userRequest)
            }.body()
            Log.d("registerUserResponse", response.toString())

            if (response.user == null){
                ApiResponse.Error("Error al registrar usuario, Reintentar mas tarde.")
            }else{
                ApiResponse.Success(response)
            }
        } catch (e: Exception) {
            Log.d("registerUserResponse Exception", e.message.toString())
            handleApiException(e)

        }
    }


    override suspend fun deleteUser(uid: String): ApiResponse<Boolean> {
        return try {
            client.post(REGISTER_USER) {
                contentType(ContentType.Application.Json)
                body = uid
            }
            ApiResponse.Success(true)
        } catch (e: Exception) {
            handleApiException(e)
        }
    }


}