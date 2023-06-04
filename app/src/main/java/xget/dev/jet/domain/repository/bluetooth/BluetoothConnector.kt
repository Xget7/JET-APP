package xget.dev.jet.domain.repository.bluetooth

import java.io.IOException

interface BluetoothConnector {
    @Throws(IOException::class)
    fun connect(): BluetoothSocketWrapper?
}