package xget.dev.jet.data.remote.auth.dto

import kotlinx.serialization.Serializable
import xget.dev.jet.data.remote.users.dto.UserResponse

@Serializable
data class LoginResponse(
    val user : UserResponse, //Maybe not correct
    val jwt : String
)
