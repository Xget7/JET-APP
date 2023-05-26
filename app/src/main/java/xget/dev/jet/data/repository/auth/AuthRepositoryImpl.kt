package xget.dev.jet.data.repository.auth

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.auth.AuthService
import xget.dev.jet.data.remote.auth.dto.LoginRequest
import xget.dev.jet.data.remote.auth.dto.LoginResponse
import xget.dev.jet.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
): AuthRepository {
    override suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return authService.login(request)
    }

    override suspend fun logOut(): ApiResponse<Any> {
        return authService.logOut()
    }
}