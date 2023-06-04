package xget.dev.jet.domain.repository.user

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.dto.UserRequest
import xget.dev.jet.data.remote.users.dto.UserResponse
import xget.dev.jet.domain.model.user.RegisterUser
import xget.dev.jet.domain.model.user.User

interface UserRepository {
    suspend fun getUser(uid: String): ApiResponse<UserResponse>?
    suspend fun registerUser(userRequest: UserRequest): ApiResponse<UserResponse>?

    suspend fun deleteUser(uid: String) : ApiResponse<Boolean>

    fun isValidUser(user: RegisterUser): Pair<String,Boolean>
}