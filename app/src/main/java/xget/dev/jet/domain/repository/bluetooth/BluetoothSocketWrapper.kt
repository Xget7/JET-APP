package xget.dev.jet.domain.repository.bluetooth

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

interface BluetoothSocketWrapper {
    @get:Throws(IOException::class)
    val inputStream: InputStream?

    @get:Throws(IOException::class)
    val outputStream: OutputStream?
    val remoteDeviceName: String?

    @Throws(IOException::class)
    fun connect()
    val remoteDeviceAddress: String?

    @Throws(IOException::class)
    fun close()
    val underlyingSocket: BluetoothSocket
}