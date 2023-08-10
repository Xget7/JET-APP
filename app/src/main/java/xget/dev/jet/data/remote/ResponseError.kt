package xget.dev.jet.data.remote

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseError(
    @SerializedName("message")
    val message: String = ""
)
