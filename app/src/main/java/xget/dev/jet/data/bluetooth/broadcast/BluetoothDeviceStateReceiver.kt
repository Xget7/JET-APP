package xget.dev.jet.data.bluetooth.broadcast

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BluetoothDeviceStateReceiver(
    private val onStateChanged: (isConnected: Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val device: BluetoothDevice? =
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)


        when (intent?.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                Log.d("Device ACTION_CONNECTED", device.toString())

                onStateChanged(true)
            }

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                Log.d("Device ACTION_DISCONNECTED", device.toString())

                onStateChanged(false)
            }
        }

    }

}

class BluetoothStateReceiver(
    private val onStateChanged: (isBluetoothEnabled: Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            when (intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_OFF -> {
                    onStateChanged(false)
                    Log.i("Bluetooth", "State OFF")
                }

                BluetoothAdapter.STATE_ON -> {
                    onStateChanged(true)
                    Log.i("Bluetooth", "State ON")
                }
            }
        }
    }

}
