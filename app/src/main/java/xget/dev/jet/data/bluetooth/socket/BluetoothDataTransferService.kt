package xget.dev.jet.data.bluetooth.socket

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import xget.dev.jet.data.bluetooth.socketsUtils.TransferFailedException
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnectionResult
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class BluetoothDataTransferService(
    val socket: BluetoothSocket
) {

    private val mmInStream : InputStream = socket.inputStream
    private val mmOutStream : OutputStream = socket.outputStream
    private val mmBuffer : ByteArray = ByteArray(1024)


    fun listenForIncomingMessages(): Flow<String> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            while (true) {
                val byteCount = try {
                    //try to receive
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    throw TransferFailedException()
                }
                emit(
                    mmBuffer.decodeToString(
                        endIndex = byteCount
                    )
                )
            }
            //Flow on dispatchers in output
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessageToDevice(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("sendMessage", bytes.toString())
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.w("sendMessage FAIL ", "IOEX", e)
                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }


}