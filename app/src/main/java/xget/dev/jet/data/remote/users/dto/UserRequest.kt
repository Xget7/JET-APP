package xget.dev.jet.data.remote.users.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val name : String = "",
    val gmail : String= "",
    val password : String= "",
    val phoneNumber : String = "",
)
