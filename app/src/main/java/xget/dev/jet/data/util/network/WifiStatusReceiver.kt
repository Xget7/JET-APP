package xget.dev.jet.data.util.network

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log

class WifiStatusReceiver (
private val onWifiChange: (Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION ->{
                if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                    //stuff
                    onWifiChange(true)
                } else {
                    // wifi connection was lost
                    onWifiChange(false)
                }
            }
        }
    }
}