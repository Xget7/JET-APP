package xget.dev.jet.domain.model.device

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SmartDeviceAction(
    val time : String ,
    val state : Boolean,
    @SerializedName("user")
    val userName : String
)