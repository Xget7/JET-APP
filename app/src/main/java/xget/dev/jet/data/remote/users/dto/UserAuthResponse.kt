package xget.dev.jet.data.remote.users.dto


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class UserAuthResponse(
    @SerializedName("id")
    val id: String?= "",
    @SerializedName("token")
    val token: String?= ""
)
