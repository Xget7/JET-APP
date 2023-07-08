package xget.dev.jet.presentation.main.history

import androidx.compose.runtime.mutableIntStateOf
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.domain.model.device.SmartDevice

data class HistoryState(
    val isLoading: Boolean = false,
    val isError: String? = null,
    val devices: List<DeviceDto> = mutableListOf()

)
