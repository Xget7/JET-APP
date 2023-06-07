package xget.dev.jet.data.remote.users.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class UserRequest(
    val username : String = "",
    val email : String= "",
    val password : String= "",

    @SerialName("last_login")
    val lastLogin : String? = "",

    val profilePicture : String= "",
    val phoneNumber : String = "",

    @SerialName("is_active")
    var isActive: Boolean = false,
    @SerialName("is_staff")
    var isStaff: Boolean = false
)
