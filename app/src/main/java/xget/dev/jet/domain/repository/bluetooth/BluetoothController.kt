package xget.dev.jet.domain.repository.bluetooth;

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Thread.State

interface BluetoothController {

    val isConnected : StateFlow<Boolean>
    val isBluetoothOn : StateFlow<Boolean>
    val pairedDevice : StateFlow<BluetoothDevice?>
    val scannedDevices : StateFlow<List<BluetoothDevice>?>
    val errors : SharedFlow<String> //One time events
    fun startDiscovery()
    fun stopDiscovery()
    suspend fun trySendMessage(message: String): String?
    fun closeConnection()
    fun release()
    fun connectToDevice(): Flow<BluetoothConnectionResult>
}
