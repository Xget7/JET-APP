package xget.dev.jet.domain.repository.user

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.dto.RegisterRequest
import xget.dev.jet.data.remote.users.dto.UserAuthResponse
import xget.dev.jet.domain.model.user.RegisterUser
import xget.dev.jet.domain.model.user.User

interface UserRepository {
    suspend fun getUser(): ApiResponse<User>?
    suspend fun registerUser(registerRequest: RegisterRequest): ApiResponse<UserAuthResponse>?

    suspend fun deleteUser(uid: String) : ApiResponse<Boolean>

    fun isValidUser(user: RegisterUser): Pair<String,Boolean>
}