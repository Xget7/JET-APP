package xget.dev.jet.data.remote.devices.rest.dto.history

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class DeviceActionRequest(
    val state : Boolean ,
    @SerializedName("user_id")
    val userId : String ,
    @SerializedName("device_id")
    val deviceId : String
)
