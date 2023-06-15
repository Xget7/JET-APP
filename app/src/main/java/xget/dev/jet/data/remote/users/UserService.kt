package xget.dev.jet.data.remote.users

import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.data.remote.users.dto.RegisterRequest
import xget.dev.jet.data.remote.users.dto.UserAuthResponse

interface UserService {

    suspend fun getUser(uid : String) : ApiResponse<UserAuthResponse>?

    suspend fun registerUser(registerRequest : RegisterRequest) : ApiResponse<UserAuthResponse>?
    suspend fun deleteUser(uid : String) : ApiResponse<Boolean>


}