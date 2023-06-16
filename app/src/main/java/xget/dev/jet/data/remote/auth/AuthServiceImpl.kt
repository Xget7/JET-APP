package xget.dev.jet.data.remote.auth

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters
import xget.dev.jet.data.remote.HttpRoutes.FORGOT_PASSWORD
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.HttpRoutes.LOGIN_USER
import xget.dev.jet.data.remote.HttpRoutes.LOGOUT_USER
import xget.dev.jet.data.remote.auth.dto.forgotPass.ForgotPasswordRequest
import xget.dev.jet.data.remote.auth.dto.login.LoginRequest
import xget.dev.jet.data.remote.users.dto.UserAuthResponse
import xget.dev.jet.data.util.network.handleApiCodeStatusException
import xget.dev.jet.data.util.network.handleApiException
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    val client : HttpClient,
    private val token: Token
): AuthService {

    override suspend fun login(request: LoginRequest): ApiResponse<UserAuthResponse> {
        ApiResponse.Loading<Any>()
        return try {
            Log.d("LoginSentRequest", request.toString())
            val response = client.post(LOGIN_USER){
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            Log.d("LoginResponse", response.bodyAsText())
            Log.d("LoginResponse", response .toString())

            val formatedResponse = response.body<UserAuthResponse>()
            Log.d("FormatedLoginResponse", formatedResponse.toString())


            if (formatedResponse.token != null){
                if (formatedResponse.id.isNullOrBlank()){
                    ApiResponse.Error("Error con la autenticacion del usuario.")
                } else {
                    Log.d("settedJwt", formatedResponse.token)
                    token.setJwtLocal(formatedResponse.token ?: "" , formatedResponse.id)
                    ApiResponse.Success(formatedResponse)
                }
            } else {
                handleApiCodeStatusException(response.status)
            }

        }  catch (e: Exception) {
            Log.d("ExceptonPure", e.message .toString())
            handleApiException(e)
        }
    }

    override suspend fun sendForgotPasswordEmail(request: ForgotPasswordRequest): ApiResponse<Boolean> {
        ApiResponse.Loading<Any>()
        return try {
            val response = client.post(FORGOT_PASSWORD){
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status == HttpStatusCode.OK){
                ApiResponse.Success(true)
            }else{
                ApiResponse.Error("Hubo un error al intentar enviarte un correo electronico.")
            }

        }  catch (e: Exception) {
            handleApiException(e)
        }
    }

    override suspend fun logOut(): ApiResponse<Any> {
        ApiResponse.Loading<Any>()
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