package xget.dev.jet.data.remote.users.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import xget.dev.jet.domain.model.user.User
import xget.dev.jet.domain.repository.token.Token

@Serializable
data class UserResponse(
   val user : User?,
   @SerialName("is_active")
   var isActive: Boolean = false,
   @SerialName("is_staff")
   var isStaff: Boolean = false
)
