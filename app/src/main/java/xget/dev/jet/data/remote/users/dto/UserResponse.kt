package xget.dev.jet.data.remote.users.dto

import kotlinx.serialization.Serializable
import xget.dev.jet.domain.model.user.User
@Serializable
data class UserResponse(
   val user : User
)
