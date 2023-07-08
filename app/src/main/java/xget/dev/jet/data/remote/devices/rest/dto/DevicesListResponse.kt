package xget.dev.jet.data.remote.devices.rest.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DevicesListResponse(
    @SerializedName("my_devices")
    val myDevices : List<DeviceDto>,
    @SerializedName("devices")
    val allDevices : List<DeviceDto>
)
