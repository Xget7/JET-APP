package xget.dev.jet.data.remote.auth.dto.forgotPass

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRequest(
    val email : String
)
