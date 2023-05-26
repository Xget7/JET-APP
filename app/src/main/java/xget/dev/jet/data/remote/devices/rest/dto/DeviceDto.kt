package xget.dev.jet.data.remote.devices.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id : String,
    val userId : String,
    val deviceType : String //can create sealed class
)
