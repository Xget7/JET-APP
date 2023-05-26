package xget.dev.jet.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid : String,
    val name : String,
    val gmail : String,
    val phoneNumber : String,
)
