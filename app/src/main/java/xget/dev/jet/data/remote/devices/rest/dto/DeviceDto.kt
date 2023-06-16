package xget.dev.jet.data.remote.devices.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id : String ="",
    @SerialName("name")
    val name : String = "",
    @SerialName("id_user_main")
    val userId : String = "",
    @SerialName("device_type")
    val deviceType : String  = "",//can create sealed class,
    @SerialName("users_id")
    val accessUsersIds: List<String> = emptyList()
)
