package xget.dev.jet.data.remote.devices.rest.dto.history

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import xget.dev.jet.domain.model.device.SmartDeviceAction

@Serializable
data class DeviceHistoryResponse(
    val history: List<SmartDeviceAction>
)

