package xget.dev.jet.presentation.main.history.device_history

import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto

data class DeviceHistoryState(
    val isLoading: Boolean = false,
    val isError: String? = null,
    val devices: List<DeviceDto> = mutableListOf()

)
