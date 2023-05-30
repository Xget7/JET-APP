package xget.dev.jet.data.remote.auth.dto

import kotlinx.serialization.Serializable
import xget.dev.jet.data.remote.users.dto.UserResponse
import xget.dev.jet.domain.model.user.User

@Serializable
data class LoginResponse(
    val user : User, //Maybe not correct
    val jwt : String
)
