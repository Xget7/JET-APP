package xget.dev.jet.domain.repository.bluetooth

sealed interface BluetoothConnectionResult {
    object ConnectionEstablished : BluetoothConnectionResult
    data class Error(val errorMsg : String) : BluetoothConnectionResult
    data class TransferSucceeded (val message: String) : BluetoothConnectionResult
}