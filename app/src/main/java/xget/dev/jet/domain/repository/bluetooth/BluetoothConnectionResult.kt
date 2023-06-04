package xget.dev.jet.domain.repository.bluetooth

import android.telecom.ConnectionRequest

sealed interface BluetoothConnectionResult {
    object ConnectionEstablished : BluetoothConnectionResult
    data class Error(val message : String) : BluetoothConnectionResult
    data class TransferSucceeded (val message: String) : BluetoothConnectionResult
}