package xget.dev.jet.data.remote.users.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username : String = "",
    val email : String= "",
    val password : String= "",
    val profilePicture : String = "null",
    val phoneNumber : String = "",
)
