package xget.dev.jet.presentation.main.history.device_history

import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.domain.model.device.SmartDeviceAction
import xget.dev.jet.presentation.main.history.HistoryState

data class DeviceHistoryState(
    val isLoading: Boolean = false,
    val isError: String? = null,
    val device: DeviceDto = DeviceDto(),
    val history: List<SmartDeviceAction> = listOf()

)
