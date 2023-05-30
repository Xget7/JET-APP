package xget.dev.jet.data.util.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter

open class Event<out T>(val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }


    fun peekContent(): T = content



}

object BluetoothUtils {

    const val SPP_UUID = "00001101-0000-1000-8000-00805f9b34fb"
}
