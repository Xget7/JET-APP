package xget.dev.jet.data.bluetooth.socketsUtils

import java.io.IOException

class FallbackException(e: Exception?) : Exception(e) {
    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1L
    }
}

class TransferFailedException: IOException("Reading incoming data failed")