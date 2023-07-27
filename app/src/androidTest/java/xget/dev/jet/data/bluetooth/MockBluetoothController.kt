package xget.dev.jet.data.bluetooth

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import xget.dev.jet.data.bluetooth.socket.BluetoothDataTransferService
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnectionResult
import xget.dev.jet.domain.repository.bluetooth.BluetoothController

class MockBluetoothController() : BluetoothController {

    private val _scannedDevices =
        MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDevice>?>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevice =
        MutableStateFlow<BluetoothDevice?>(null)
    override val pairedDevice: StateFlow<BluetoothDevice?>
        get() =
            _pairedDevice.asStateFlow()

    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()


    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> get() = _isConnected.asStateFlow()

    private val _isBluetoothOn = MutableStateFlow(false)
    override val isBluetoothOn: StateFlow<Boolean> get() = _isBluetoothOn.asStateFlow()

    override fun startDiscovery() {

    }

    override fun stopDiscovery() {

    }

    override suspend fun trySendMessage(message: String): String? {
        return "yes"
    }

    override fun closeConnection() {
    }

    override fun release() {
    }

    override fun connectToDevice(): Flow<BluetoothConnectionResult> =
        flow {
            emit(BluetoothConnectionResult.ConnectionEstablished)
            kotlinx.coroutines.delay(2000)
            emit(BluetoothConnectionResult.TransferSucceeded("trasnferMessage"))
        }


}