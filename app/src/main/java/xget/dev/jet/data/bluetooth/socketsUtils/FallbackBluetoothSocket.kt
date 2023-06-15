package xget.dev.jet.data.bluetooth.socketsUtils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


@SuppressLint("MissingPermission")
class FallbackBluetoothSocket(tmp: BluetoothSocket) :
    NativeBluetoothSocket(tmp) {
    private var fallbackSocket: BluetoothSocket? = null

    init {
        fallbackSocket = try {
            val clazz: Class<*> = tmp.remoteDevice.javaClass
            val paramTypes = arrayOf<Class<*>>(Integer.TYPE)
            val m = clazz.getMethod("createRfcommSocket", *paramTypes)
            val params = arrayOf<Any>(Integer.valueOf(1))
            m.invoke(tmp.remoteDevice, *params) as BluetoothSocket
        } catch (e: Exception) {
            throw FallbackException(e)
        }
    }

    @get:Throws(IOException::class)
    override val inputStream: InputStream?
        get() = fallbackSocket!!.inputStream

    @get:Throws(IOException::class)
    override val outputStream: OutputStream?
        get() = fallbackSocket!!.outputStream



    @Throws(IOException::class)
    override fun connect() {
        fallbackSocket!!.connect()
    }

    @Throws(IOException::class)
    override fun close() {
        fallbackSocket!!.close()
    }
}