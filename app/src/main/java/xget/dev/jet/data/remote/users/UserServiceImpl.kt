package xget.dev.jet.data.remote.users

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import xget.dev.jet.data.util.HttpRoutes.GET_USER
import xget.dev.jet.data.util.HttpRoutes.REGISTER_USER
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.dto.RegisterRequest
import xget.dev.jet.data.remote.users.dto.UserAuthResponse
import xget.dev.jet.data.util.network.handleApiException
import xget.dev.jet.domain.model.user.User
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject

@OptIn(InternalAPI::class)
class UserServiceImpl @Inject constructor(
    private val client: HttpClient,
    private val token: Token
) : UserService {

    private val currentToken = token.getJwtLocal()
    override suspend fun getUser(): ApiResponse<User>? {
        ApiResponse.Loading<Any>()
        return try {
            if (currentToken != null) {
                Log.d("userTOkenGetUser", currentToken)
            }
            val response = client.get(GET_USER) {
                header("Authorization", "Bearer $currentToken")
            }
            Log.d("response", response.body())
            Log.d("response", response.status.toString())
            ApiResponse.Success(response.body<User>())
        } catch (e: Exception) {
            handleApiException(e)
        }
    }

    override suspend fun registerUser(registerRequest: RegisterRequest): ApiResponse<UserAuthResponse>? {
        ApiResponse.Loading<Any>()
        return try {

            val response = client.post(REGISTER_USER) {
                contentType(ContentType.Application.Json)
                setBody(registerRequest)
            }

            val formatedResponse = response.body<UserAuthResponse>()
            if (formatedResponse.id == null) {
                ApiResponse.Error("Error al registrar usuario, reintentar mas tarde.")
            } else {
                token.setJwtLocal(formatedResponse.token ?: "null", formatedResponse.id)
                ApiResponse.Success(formatedResponse)
            }
        } catch (e: Exception) {
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