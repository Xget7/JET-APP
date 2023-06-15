package xget.dev.jet.data.bluetooth.socket

import android.bluetooth.BluetoothSocket
import com.google.android.gms.common.ConnectionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import xget.dev.jet.data.bluetooth.socketsUtils.TransferFailedException
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnectionResult
import java.io.IOException

class BluetoothDataTransferService(
    val socket: BluetoothSocket
) {

    fun listenForIncomingMessages(): Flow<String> {

        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)
            while (true) {
                val byteCount = try {
                    //try to receive
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    throw TransferFailedException()
                }
                emit(

                    buffer.decodeToString(
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
                socket.outputStream.write(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }


}