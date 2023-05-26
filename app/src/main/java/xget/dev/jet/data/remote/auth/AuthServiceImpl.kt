package xget.dev.jet.data.remote.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.HttpRoutes.LOGIN_USER
import xget.dev.jet.data.remote.HttpRoutes.LOGOUT_USER
import xget.dev.jet.data.remote.auth.dto.LoginRequest
import xget.dev.jet.data.remote.auth.dto.LoginResponse
import xget.dev.jet.data.util.network.handleApiException
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    val client : HttpClient,
    private val token: Token
): AuthService {

    override suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return try {
            val response : LoginResponse = client.post(LOGIN_USER){
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
            token.setJwtLocal(response.jwt)
            ApiResponse.Success(response)
        }  catch (e: Exception) {
            handleApiException(e)
        }
    }

    override suspend fun logOut(): ApiResponse<Any> {
        return try {
            client.post(LOGOUT_USER){
                header("Authorization", "Bearer ${token.getJwtLocal()}")
            }
            ApiResponse.Success(0)
        }  catch (e: Exception) {
            handleApiException(e)
        }
    }




}