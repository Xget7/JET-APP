package xget.dev.jet.domain.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id : String = "",
    val password : String ="",
    @SerialName("last_login")
    val lastLogin : String? = null,
    @SerialName("username")
    val userName : String = "",
    val email : String = "",
    val profilePicture : String? = "",
    val phoneNumber : String = "",


)
