package xget.dev.jet.data.remote.auth.dto.login

import kotlinx.serialization.*


@Serializable
data class LoginRequest(
    val email : String,
    val password : String
)
