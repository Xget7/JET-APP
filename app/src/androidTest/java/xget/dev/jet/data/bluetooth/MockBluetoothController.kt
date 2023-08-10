package xget.dev.jet.data.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.IBinder
import android.os.Parcel
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.data.bluetooth.socket.BluetoothDataTransferService
import xget.dev.jet.data.repository.bluetooth.hasPermission
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnectionResult
import xget.dev.jet.domain.repository.bluetooth.BluetoothController

class MockBluetoothController(
    val context: Context
) : BluetoothController {

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
        if (hasPermission(Manifest.permission.BLUETOOTH_SCAN, context)) {
            Log.d(
                "HasPermission",
                hasPermission(Manifest.permission.BLUETOOTH_SCAN, context).toString()
            )
            return
        }
    }

    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    private val bluetoothAdapter = bluetoothManager?.adapter
    private val mockedBluetoothDevice = bluetoothAdapter?.getRemoteDevice("00:11:22:33:FF:EE")!!

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _scannedDevices.update { devices ->
                if (mockedBluetoothDevice in devices) devices else devices + mockedBluetoothDevice
            }
            delay(2000)
            mockedBluetoothDevice.name?.let { deviceName ->
                if (deviceName.contains("JET")) {
                    if (bluetoothAdapter?.bondedDevices?.contains(mockedBluetoothDevice) == true) {
                        Log.d("FoundDeviceReceiver", "${mockedBluetoothDevice.name} is Bonded")
                        delay(1500)
                        _pairedDevice.update {
                            mockedBluetoothDevice
                        }
                    } else {
                        Log.d("FoundDeviceReceiver", "${mockedBluetoothDevice.name} is NOT Bonded")
                        if (mockedBluetoothDevice.createBond()) {
                            Log.d("Create Bond == true", "added device")
                            delay(1500)
                            _pairedDevice.update {
                                _scannedDevices.value.first { it.name.contains("JET") }
                            }
                            delay(1500)
                            _isConnected.update { true }
                        } else {
                            Log.d("Create Bond == False", "not device")
                            _errors.emit("No se pudo vincular dispositivo , hazlo manualmente y vuelve a intenetarlo.")
                            delay(1500)
                            _isConnected.update { false }
                        }

                    }
                }
            }
        }

    }

    override fun stopDiscovery() {
        Log.d("TEST", "Stop Discovery")
    }

    override suspend fun trySendMessage(message: String): String? {
        return "yes"
    }

    override fun closeConnection() {
        Log.d("TEST", "closeConnection")
    }

    override fun release() {
        Log.d("TEST", "release")

    }

    override fun connectToDevice(): Flow<BluetoothConnectionResult> {
        return flow {
            if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT, context)) {
                throw SecurityException("No hay permisos suficientes para establecer una conexion.")
            }

            emit(BluetoothConnectionResult.ConnectionEstablished)

            delay(2000)

            emit(BluetoothConnectionResult.TransferSucceeded("trasnferMessage"))
        }


    }

}

