package xget.dev.jet.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object ConstantsShared {

    const val jwtIssuer = "jwt-Issuer"
    const val jwtAudience = "jwt-audience"
    const val jwtSubject = "jwt-Subject"
    const val jwtRealm = "jwt-Realm"
    const val AUTH_PREFERENCES = "auth-preferences"
    const val jwtKey = "JWT-TOKEN"

    //MQTT
    const val AWS_MQTT_ENDPOINT = "BUILDCONFIG"

    //
    const val CONNECTION_KEY = "CONNECTION_KEY"
    const val CONNECTED = "CONNECTEd"
    const val historyProperty = "history"
    const val ConnectionStatusProperty = "connectionStatus"
    const val empty = ""
    const val IsFirstTime = "IsFirstTime"
    const val LastQuantityOfDevices = "LAST_QUANTITY_OF_DEVICES"


    const val LAST_DEVICE_SELECTED = "lastDeviceSelected"
    const val LAST_DEVICE_NAME = "lastDeviceNAME"
    const val WIFI_CREDENTIALS = "wifiCredentials"
    const val USER_ID = "user_id"
}

fun secondsToMinutes(milliseconds: Long): String {

    val minutes = milliseconds / 1000 / 60
    val seconds = milliseconds / 1000 % 60
    return String.format("%d:%02d", minutes, seconds)
}

fun List<DeviceDto>.toSmartDevice(): List<SmartDevice> {
    val tempList = mutableListOf<SmartDevice>()
    for (i in this) {
        tempList.add(
            SmartDevice(
                i.id, i.userId, i.name, i.deviceType, false,
                mutableIntStateOf(2)
            )
        )
    }
    return tempList
}

fun List<Pair<DeviceDto, Pair<String, SmartDeviceMqtt>>>.convertToSmartDevice(): List<SmartDevice> {
    val tempList = mutableListOf<SmartDevice>()
    Log.d("receiverdList", this.toString())

    for (i in this) {
        tempList.add(
            SmartDevice(
                i.first.id,
                i.first.userId,
                i.first.name,
                i.first.deviceType,
                i.second.second.online == 1,
                mutableIntStateOf(i.second.second.state)
            )
        )
    }
    return tempList
}

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun convertTimestamp(timestamp: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm")

    val dateTime = LocalDateTime.parse(timestamp, inputFormatter)
    val localDateTime = dateTime.atZone(ZoneId.systemDefault()).toLocalDateTime()

    return localDateTime.format(outputFormatter)
}