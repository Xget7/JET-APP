package xget.dev.jet.domain.repository.auth

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.auth.dto.LoginRequest
import xget.dev.jet.data.remote.auth.dto.LoginResponse

interface AuthRepository {
    suspend fun login(request : LoginRequest) : ApiResponse<LoginResponse>
    suspend  fun logOut() : ApiResponse<Any>
}