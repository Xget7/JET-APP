package xget.dev.jet.data.repository.auth

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.auth.AuthService
import xget.dev.jet.data.remote.auth.dto.forgotPass.ForgotPasswordRequest
import xget.dev.jet.data.remote.auth.dto.login.LoginRequest
import xget.dev.jet.data.remote.auth.dto.login.LoginResponse
import xget.dev.jet.domain.repository.auth.AuthRepository
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val token: Token
): AuthRepository {

    override suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return authService.login(request)
    }

    override suspend fun sendForgotPasswordEmail(request: ForgotPasswordRequest): ApiResponse<Boolean> {
        return authService.sendForgotPasswordEmail(request)
    }

    override suspend fun logOut(): ApiResponse<Any> {
        return authService.logOut()
    }

    override fun isLoggedIn(): Boolean {
        return !token.getJwtLocal().isNullOrBlank()
    }


}