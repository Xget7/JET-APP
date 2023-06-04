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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.data.bluetooth.BluetoothConnectorImpl
import xget.dev.jet.data.bluetooth.broadcast.BluetoothStateReceiver
import xget.dev.jet.data.bluetooth.broadcast.FoundDeviceReceiver
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




    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> get() = _isConnected.asStateFlow()


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
    private var currentConnector: BluetoothConnectorImpl? = null

    private val _errors = MutableStateFlow("")
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            if (device in devices) devices else devices + device
        }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, bluetoothDevice ->
        if (bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
            _isConnected.update { isConnected }
        } else {
           if (bluetoothDevice.createBond()){
               _isConnected.update { true }
           }else{
               CoroutineScope(Dispatchers.IO).launch {
                   _errors.emit("No se pudo vincular dispositivo , hazlo manualmente y vuelve a intenetarlo.")
               }
               _isConnected.update { false }
           }
        }
    }

    init {
        context.registerReceiver(
            bluetoothStateReceiver,
            IntentFilter ().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
    }

    override fun startDiscovery() {
        if (hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }
        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )
        bluetoothAdapter?.startDiscovery()
    }


    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun startBluetoothServer(): Flow<BluetoothConnectionResult> {
        return flow {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No hay permisos suficientes para establecer una conexion.")
            }
        }
    }

    override fun connectToDevice(device: BluetoothDevice): Flow<BluetoothConnectionResult> {
        return flow {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No hay permisos suficientes para establecer una conexion.")
            }

            if (bluetoothAdapter != null) {
                currentConnector = BluetoothConnectorImpl(
                    device,
                    false,
                    bluetoothAdapter!!,
                    null
                )
                stopDiscovery()

                try {
                    currentServerSocket = currentConnector!!.connect()?.underlyingSocket
                    emit(BluetoothConnectionResult.ConnectionEstablished)
                } catch (e: IOException) {
                    currentConnector?.bluetoothSocket?.close()
                    emit(BluetoothConnectionResult.Error("La conexion fue interrumpida"))
                }

            } else {
                throw Exception("No se ha podido vincular el dispositivo")
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override fun closeConnection() {
        currentServerSocket?.close()
        currentConnector = null
        currentServerSocket = null
    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
        context.unregisterReceiver(bluetoothStateReceiver)
        closeConnection()
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    //SEE for ViewModel https://www.youtube.com/watch?v=9dcRWtARgmk

}