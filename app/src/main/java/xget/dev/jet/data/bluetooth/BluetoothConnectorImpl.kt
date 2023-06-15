package xget.dev.jet.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import xget.dev.jet.data.bluetooth.socketsUtils.FallbackBluetoothSocket
import xget.dev.jet.data.bluetooth.socketsUtils.FallbackException
import xget.dev.jet.data.bluetooth.socketsUtils.NativeBluetoothSocket
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnector
import xget.dev.jet.domain.repository.bluetooth.BluetoothSocketWrapper
import java.io.IOException
import java.util.*

@SuppressLint("MissingPermission")
class BluetoothConnectorImpl(
    private val device: BluetoothDevice,
    private val secure: Boolean,
    private val adapter: BluetoothAdapter,
    private var uuidCandidates: MutableList<UUID>?
)  : BluetoothConnector {
    var bluetoothSocket: BluetoothSocketWrapper? = null
    private var candidate = 0

    /**
     * @param device the device
     * @param secure if connection should be done via a secure socket
     * @param adapter the Android BT adapter
     * @param uuidCandidates a list of UUIDs. if null or empty, the Serial PP id is used
     */
    init {
        if (uuidCandidates == null || uuidCandidates.isNullOrEmpty()) {
            uuidCandidates = ArrayList()
            (uuidCandidates as ArrayList<UUID>).add(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
        }
    }

    @Throws(IOException::class)
    override fun connect(): BluetoothSocketWrapper? {
        var success = false
        while (selectSocket()) {
            adapter.cancelDiscovery()
            try {
                bluetoothSocket!!.connect()
                success = true
                break
            } catch (e: IOException) {
                //try the fallback
                try {
                    bluetoothSocket = FallbackBluetoothSocket(bluetoothSocket!!.underlyingSocket)
                    Thread.sleep(500)
                    (bluetoothSocket as FallbackBluetoothSocket).connect()
                    success = true
                    break
                } catch (e1: FallbackException) {
                    Log.w("BT", "Could not initialize FallbackBluetoothSocket classes.", e)
                } catch (e1: InterruptedException) {
                    Log.w("BT", e1.message, e1)
                } catch (e1: IOException) {
                    Log.w("BT", "Fallback failed. Cancelling.", e1)
                }
            }
        }
        if (!success) {
            throw IOException("Could not connect to device: " + device.address)
        }
        return bluetoothSocket
    }


    @Throws(IOException::class)
    private fun selectSocket(): Boolean {
        if (candidate >= uuidCandidates!!.size) {
            return false
        }
        val tmp: BluetoothSocket
        val uuid = uuidCandidates?.let { it[candidate++] }
        Log.i("BT", "Attempting to connect to Protocol: $uuid")
        tmp = if (secure) {
            device.createRfcommSocketToServiceRecord(uuid)
        } else {
            device.createInsecureRfcommSocketToServiceRecord(uuid)
        }
        bluetoothSocket = NativeBluetoothSocket(tmp)
        return true
    }
}