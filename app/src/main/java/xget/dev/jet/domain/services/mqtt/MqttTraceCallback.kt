package xget.dev.jet.domain.services.mqtt

import android.util.Log
import info.mqtt.android.service.MqttTraceHandler


internal class MqttTraceCallback : MqttTraceHandler {
    override fun traceDebug(message: String?) {
        Log.d("TIMBER",message ?: "")
    }

    override fun traceError(message: String?) {
        Log.d("TIMBER",message ?: "")
    }

    override fun traceException(message: String?, e: Exception?) {
        Log.d("TIMBER",message ?: "")
    }
}