package xget.dev.jet.data.repository.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.data.bluetooth.BluetoothConnectorImpl
import xget.dev.jet.data.bluetooth.broadcast.BluetoothDeviceStateReceiver
import xget.dev.jet.data.bluetooth.broadcast.BluetoothStateReceiver
import xget.dev.jet.data.bluetooth.broadcast.FoundDeviceReceiver
import xget.dev.jet.data.bluetooth.socket.BluetoothDataTransferService
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnectionResult
import xget.dev.jet.domain.repository.bluetooth.BluetoothController
import java.io.IOException
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BluetoothControllerImpl @Inject constructor(
    val context: Context,
) : BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private var dataTransferService: BluetoothDataTransferService? = null

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> get() = _isConnected.asStateFlow()

    private val _isBluetoothOn = MutableStateFlow(bluetoothAdapter?.isEnabled ?: false)
    override val isBluetoothOn: StateFlow<Boolean> get() = _isBluetoothOn.asStateFlow()


    private val _scannedDevices =
        MutableStateFlow<List<BluetoothDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDevice>?>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevice =
        MutableStateFlow<BluetoothDevice?>(null)
    override val pairedDevice: StateFlow<BluetoothDevice?>
        get() =
            _pairedDevice.asStateFlow()

    private var currentServerSocket: BluetoothSocket? = null
    lateinit var currentConnector: BluetoothConnectorImpl

    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private val foundDeviceReceiver = FoundDeviceReceiver { deviceFound ->
        Log.d("bluetoothDeviceStateReceiver", "Found" + deviceFound.address)
        _scannedDevices.update { devices ->
            if (deviceFound in devices) devices else devices + deviceFound
        }
        deviceFound.name?.let { deviceName ->
            if (deviceName.contains("JET")) {
                if (bluetoothAdapter?.bondedDevices?.contains(deviceFound) == true) {
                    Log.d("FoundDeviceReceiver", "${deviceFound.name} is Bonded")
                    _pairedDevice.update {
                        deviceFound
                    }
                } else {
                    Log.d("FoundDeviceReceiver", "${deviceFound.name} is NOT Bonded")
                    if (deviceFound.createBond()) {
                        Log.d("Create Bond == true", "added device")
                        _pairedDevice.update {
                            _scannedDevices.value.first { it.name.contains("JET") }
                        }
                        _isConnected.update { true }
                    } else {
                        Log.d("Create Bond == False", "not device")
                        CoroutineScope(Dispatchers.IO).launch {
                            _errors.emit("No se pudo vincular dispositivo , hazlo manualmente y vuelve a intenetarlo.")
                        }
                        _isConnected.update { false }
                    }

                }
            }
        }
    }

    private val bluetoothDeviceStateReceiver = BluetoothDeviceStateReceiver { isConnected ->
        _isConnected.update { isConnected }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver { bluetoothState ->
        _isBluetoothOn.update {
            bluetoothState
        }
    }


    init {
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            }
        )

    }

    override fun startDiscovery() {
        if (hasPermission(Manifest.permission.BLUETOOTH_SCAN,context)) {
            Log.d("HasPermission", hasPermission(Manifest.permission.BLUETOOTH_SCAN,context).toString())
            return
        }
        Log.d("HasPermission", "True Permission Scan")

        context.registerReceiver(foundDeviceReceiver,
            IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
            }
        )

        context.registerReceiver(
            bluetoothDeviceStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
        bluetoothAdapter?.startDiscovery()
    }


    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN,context)) {
            return
        }
        Log.d("cancel Discovery", "Canceled DISCOVEY")
        bluetoothAdapter?.cancelDiscovery()
    }


    override fun connectToDevice(): Flow<BluetoothConnectionResult> {
        return flow {
            if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT, context)) {
                throw SecurityException("No hay permisos suficientes para establecer una conexion.")
            }

            if (bluetoothAdapter != null) {
                Log.d("currentServerSocket", "trying connecting bluetoothAdapter")

                currentConnector = BluetoothConnectorImpl(
                    _pairedDevice.value!!,
                    false,
                    bluetoothAdapter!!,
                    null
                )

                try {
                    Log.d("currentServerSocket", "trying connecting socket")

                   val preSocket = currentConnector.connect()

                    currentServerSocket = preSocket?.underlyingSocket
                    currentServerSocket.let {
                        Log.d("currentServerSocket", "Starting Transfer services")
                        BluetoothDataTransferService(currentServerSocket!!).also { transferService ->
                            Log.d("ConnectionResult", "ConnectionStablished")
                            emit(BluetoothConnectionResult.ConnectionEstablished)
                            dataTransferService = transferService
                            emitAll(
                                transferService.listenForIncomingMessages()
                                    .map {

                                        BluetoothConnectionResult.TransferSucceeded(it)

                                    }
                            )

                        }


                    }

                } catch (e: IOException) {
                    currentConnector.bluetoothSocket?.close()
                    emit(BluetoothConnectionResult.Error("La conexion fue interrumpida"))
                }

            } else {
                throw Exception("No se ha podido vincular el dispositivo")
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun trySendMessage(message: String): String? {
        if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT, context)) {
            Log.d("dataTrasnferServiceNull", "NO PERMISSION???")
            return null
        }

        if (dataTransferService == null) {
            Log.d("dataTrasnferServiceNull", "Null")
            return null
        }


        dataTransferService?.sendMessageToDevice(message.toByteArray())
        return message
    }

    override fun closeConnection() {
        Log.d("OnCloseConnection", "Closed")
        currentServerSocket?.close()
        currentServerSocket = null
    }

    override fun release() {
        context.unregisterReceiver(bluetoothDeviceStateReceiver)
        context.unregisterReceiver(bluetoothStateReceiver)
        context.unregisterReceiver(foundDeviceReceiver)
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter?.cancelDiscovery();
        }
        closeConnection()
    }




}
fun hasPermission(permission: String, context: Context): Boolean {
    return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}