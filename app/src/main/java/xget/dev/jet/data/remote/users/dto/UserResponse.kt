package xget.dev.jet.data.remote.users.dto

import kotlinx.serialization.Serializable
import xget.dev.jet.domain.model.user.User
import xget.dev.jet.domain.repository.token.Token

@Serializable
data class UserResponse(
   val user : User,
   val token: String
)
