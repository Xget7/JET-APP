package xget.dev.jet.data.remote.users

import xget.dev.jet.data.remote.ApiResponse
import xget.dev.jet.data.remote.users.dto.UserRequest
import xget.dev.jet.data.remote.users.dto.UserResponse

interface UserService {

    suspend fun getUser(uid : String) : ApiResponse<UserResponse>?

    suspend fun registerUser(userRequest : UserRequest) : ApiResponse<UserResponse>?


}