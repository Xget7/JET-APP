package xget.dev.jet.domain.repository.auth

import xget.dev.jet.data.remote.auth.dto.forgotPass.ForgotPasswordRequest
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.auth.dto.login.LoginRequest
import xget.dev.jet.data.remote.users.dto.UserAuthResponse

interface AuthRepository {
    suspend fun login(request : LoginRequest) : ApiResponse<UserAuthResponse>
    suspend fun sendForgotPasswordEmail(request : ForgotPasswordRequest) : ApiResponse<Boolean>
    suspend  fun logOut() : ApiResponse<Any>

    fun isLoggedIn() : Boolean
}