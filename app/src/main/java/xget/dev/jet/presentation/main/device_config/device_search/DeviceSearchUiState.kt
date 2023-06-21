package xget.dev.jet.presentation.main.device_config.device_search

import android.bluetooth.BluetoothDevice

data class DeviceSearchUiState(
    val searchingDevice : Boolean = false,
    val bluetoothOn : Boolean = false,
    val pairingDevice : Boolean = false,
    val cantFindDevice : Boolean = false,
    val pairedDevice : BluetoothDevice? = null,
    val errorMessage : String? = null,
    val finished : Boolean? =true,
    val syncingWithCloud : Boolean = false,
    val timeUntilStop : String = ""

)
