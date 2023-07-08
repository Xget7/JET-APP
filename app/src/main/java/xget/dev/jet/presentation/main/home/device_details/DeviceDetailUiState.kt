package xget.dev.jet.presentation.main.home.device_details

import xget.dev.jet.domain.model.device.SmartDevice

data class DeviceDetailUiState (
    val isLoading : Boolean = false,
    val smartDevice: SmartDevice = SmartDevice(),
    val msgError : String? = null
)