package xget.dev.jet.data.remote.auth

import xget.dev.jet.data.remote.auth.dto.forgotPass.ForgotPasswordRequest
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.auth.dto.login.LoginRequest
import xget.dev.jet.data.remote.auth.dto.login.LoginResponse

interface AuthService {

    suspend fun login(request : LoginRequest) : ApiResponse<LoginResponse>
    suspend fun sendForgotPasswordEmail(request : ForgotPasswordRequest) : ApiResponse<Boolean>
    suspend fun logOut(): ApiResponse<Any>
}