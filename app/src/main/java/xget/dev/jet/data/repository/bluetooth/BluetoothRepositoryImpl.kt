package xget.dev.jet.data.repository.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_BOND_STATE_CHANGED
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import xget.dev.jet.JetApp
import xget.dev.jet.data.util.bluetooth.BluetoothUtils.SPP_UUID
import xget.dev.jet.data.util.bluetooth.Event
import xget.dev.jet.domain.repository.bluetooth.BluetoothRepository
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.util.UUID
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BluetoothRepositoryImpl @Inject constructor(
    val context : Context
) : BluetoothRepository {
    var connected: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    private var progressState: MutableStateFlow<String> = MutableStateFlow("")
    val putTxt: MutableStateFlow<String> = MutableStateFlow("")

    val inProgress = MutableStateFlow(Event(false))
    val connectError = MutableStateFlow(Event(false))
    val uiConectionError = MutableStateFlow("")


    var mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var mBluetoothStateReceiver: BroadcastReceiver? = null


    var targetDevice: BluetoothDevice? = null
    private var socket: BluetoothSocket? = null
    private var mOutputStream: OutputStream? = null
    private var mInputStream: InputStream? = null



    var foundDevice:Boolean = false

    private lateinit var sendByte:ByteArray
    var discovery_error = false

    fun isBluetoothSupport():Boolean{
        return mBluetoothAdapter != null
    }

    fun isBluetoothEnabled():Boolean{
        return mBluetoothAdapter!!.isEnabled
    }

    fun scanDevice(){
        progressState.value = "Buscando dipositivos..."
        registerBluetoothReceiver()
        val bluetoothAdapter = mBluetoothAdapter
        foundDevice = false
        bluetoothAdapter?.startDiscovery()
    }


    /**
     * Registers the Bluetooth receiver for handling Bluetooth events.
     */

    private fun registerBluetoothReceiver() {
        val stateFilter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        }

        mBluetoothStateReceiver = object : BroadcastReceiver() {
            @SuppressLint("MissingPermission")
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                if (action != null) {
                    Log.d("Bluetooth action", action)
                }
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                var name: String? = null
                if (device != null) {
                    name = device.name
                }
                when (action) {
                    BluetoothAdapter.ACTION_STATE_CHANGED -> {
                        val state = intent.getIntExtra(
                            BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR
                        )
                        when (state) {
                            BluetoothAdapter.STATE_OFF -> {
                                // Handle Bluetooth turned off state
                            }
                            BluetoothAdapter.STATE_TURNING_OFF -> {
                                // Handle Bluetooth turning off state
                            }
                            BluetoothAdapter.STATE_ON -> {
                                // Handle Bluetooth turned on state
                                Log.d("BluetoothState" , "STATE_ON")

                            }
                            BluetoothAdapter.STATE_TURNING_ON -> {
                                // Handle Bluetooth turning on state
                                Log.d("BluetoothState" , "STATE_TURNING_ON")

                            }
                        }
                    }
                    BluetoothDevice.ACTION_ACL_CONNECTED -> {
                        // Handle Bluetooth device connected
                        Log.d("BluetoothState" , "ACTION_ACL_CONNECTED")

                    }
                    BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                        // Handle Bluetooth bond state changed
                        Log.d("BluetoothState" , "ACTION_BOND_STATE_CHANGED")
                    }
                    BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                        connected.value = false
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        // Handle Bluetooth discovery started
                        Log.d("BluetoothState" , "ACTION_DISCOVERY_STARTED")

                    }
                    BluetoothDevice.ACTION_FOUND -> {
                        if (!foundDevice) {
                            val deviceName = device?.name
                            val deviceAddress = device?.address

                            if (deviceName != null && deviceName.length > 4) {
                                if (deviceName.substring(0, 3) == "RNM") {
                                    targetDevice = device
                                    foundDevice = true
                                    connectToTargetedDevice(targetDevice)
                                }
                            }
                        }
                    }
                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        if (!foundDevice) {
                            uiConectionError.value = "No se pudo encontrar el dispositivo. Por favor intentalo de vuelta."
                            inProgress.value = (Event(false))
                        }
                    }
                }
            }
        }

        JetApp().registerReceiver(
            mBluetoothStateReceiver,
            stateFilter
        )
    }

    /**
     * Connects to the targeted Bluetooth device.
     */

    @ExperimentalUnsignedTypes
    private fun connectToTargetedDevice(targetedDevice: BluetoothDevice?) {
        progressState.value = ("${targetDevice?.name}에 연결중..")
        val thread = Thread {
            val uuid = UUID.fromString(SPP_UUID)
            try {
                socket = targetedDevice?.createRfcommSocketToServiceRecord(uuid)
                socket?.connect()

                connected.value = true
                mOutputStream = socket?.outputStream
                mInputStream = socket?.inputStream
                beginListenForData()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                connectError.value = Event(true)
                try {
                    socket?.close()
                } catch (e: IOException) {
                    uiConectionError.value = "Error tratanto de conectarse al dispositivo."
                    e.printStackTrace()
                }
            }
        }

        thread.start()
    }

    /**
     * Disconnects from the Bluetooth device.
     */
    fun disconnect() {
        try {
            socket?.close()
            connected.value = (false)
        } catch (e: IOException) {
            e.printStackTrace()
            uiConectionError.value = "Error al desconectar dispositivo."
        }
    }

    /**
     * Unregisters the Bluetooth receiver.
     */
    fun unregisterReceiver() {
        if (mBluetoothStateReceiver != null) {
            JetApp().unregisterReceiver(mBluetoothStateReceiver)
            mBluetoothStateReceiver = null
        }
    }

    /**
     * Sends byte data over Bluetooth.
     */
    fun sendByteData(data: ByteArray) {
        Thread {
            try {
                mOutputStream?.write(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    /**
     * Converts byte array to hexadecimal string.
     */
    fun byteArrayToHex(a: ByteArray): String? {
        val sb = StringBuilder()
        for (b in a) sb.append(String.format("%02x ", b /*&0xff*/))
        return sb.toString()
    }

    /**
     * Listens for Bluetooth data.
     */
    @ExperimentalUnsignedTypes
    fun beginListenForData() {
        val mWorkerThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    val bytesAvailable = mInputStream?.available()
                    if (bytesAvailable != null) {
                        if (bytesAvailable > 0) {
                            val packetBytes = ByteArray(bytesAvailable)
                            mInputStream?.read(packetBytes)

                            val s = String(packetBytes, Charsets.UTF_8)
                            putTxt.value = s

                            for (i in 0 until bytesAvailable) {
                                val b = packetBytes[i]
                                Log.d("inputData", String.format("%02x", b))
                            }
                        }
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    uiConectionError.value = "Error vinculandose con bluetooth."
                } catch (e: IOException) {
                    e.printStackTrace()
                    uiConectionError.value = "Error de conexion con bluetooth."
                }
            }
        }

        mWorkerThread.start()
    }





}