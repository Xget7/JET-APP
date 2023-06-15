package xget.dev.jet.presentation.main.device_config.firstStep

import androidx.activity.result.IntentSenderRequest

data class AddDeviceFirstStepUiState(
    val bluetoothOn : Boolean = true,
    val gpsOn : Boolean = false,
    val selectedItem : String = "",
    val intentSenderForResult:  IntentSenderRequest? = null
)
