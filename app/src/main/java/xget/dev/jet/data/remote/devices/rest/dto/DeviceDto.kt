package xget.dev.jet.data.remote.devices.rest.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id : String ="",
    @SerializedName("name")
    val name : String = "",
    @SerializedName("id_user_main")
    val userId : String = "",
    @SerializedName("device_type")
    val deviceType : String  = "",//can create sealed class,
    @SerializedName("users_id")
    val accessUsersIds: List<String> = emptyList()
)
