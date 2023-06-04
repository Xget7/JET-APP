package xget.dev.jet.data.bluetooth.socketsUtils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import xget.dev.jet.domain.repository.bluetooth.BluetoothSocketWrapper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("MissingPermission")
open class NativeBluetoothSocket(override val underlyingSocket: BluetoothSocket) :
    BluetoothSocketWrapper {

    @get:Throws(IOException::class)
    override val inputStream: InputStream?
        get() = underlyingSocket.inputStream

    @get:Throws(IOException::class)
    override val outputStream: OutputStream?
        get() = underlyingSocket.outputStream
    override val remoteDeviceName: String?
        get() = underlyingSocket.remoteDevice.name


    @Throws(IOException::class)
    override fun connect() {
        underlyingSocket.connect()
    }

    override val remoteDeviceAddress: String?
        get() = underlyingSocket.remoteDevice.address

    @Throws(IOException::class)
    override fun close() {
        underlyingSocket.close()
    }
}