package xget.dev.jet.domain.model.user

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id : String = "",
    @SerializedName("username")
    val userName : String = "",
    val email : String = "",
    val profilePicture : String? = "",
    val phoneNumber : String = "",
)
