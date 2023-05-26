package xget.dev.jet.data.repository.user

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.UserService
import xget.dev.jet.data.remote.users.dto.UserRequest
import xget.dev.jet.data.remote.users.dto.UserResponse
import xget.dev.jet.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteService : UserService
) : UserRepository {
    override suspend fun getUser(uid: String): ApiResponse<UserResponse>? {
       return userRemoteService.getUser(uid)
    }

    override suspend fun registerUser(userRequest: UserRequest): ApiResponse<UserResponse>? {
        return userRemoteService.registerUser(userRequest)
    }

    override suspend fun deleteUser(uid: String): ApiResponse<Boolean> {
        return userRemoteService.deleteUser(uid)
    }
}