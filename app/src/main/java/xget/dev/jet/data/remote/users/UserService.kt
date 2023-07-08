package xget.dev.jet.data.remote.users

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.dto.RegisterRequest
import xget.dev.jet.data.remote.users.dto.UserAuthResponse
import xget.dev.jet.domain.model.user.User

interface UserService {

    suspend fun getUser() : ApiResponse<User>?

    suspend fun registerUser(registerRequest : RegisterRequest) : ApiResponse<UserAuthResponse>?
    suspend fun deleteUser(uid : String) : ApiResponse<Boolean>


}