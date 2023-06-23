package xget.dev.jet.core.utils

object ConstantsShared {

    const val jwtIssuer = "jwt-Issuer"
    const val jwtAudience = "jwt-audience"
    const val jwtSubject = "jwt-Subject"
    const val jwtRealm = "jwt-Realm"
    const val AUTH_PREFERENCES = "auth-preferences"
    const val jwtKey  = "JWT-TOKEN"

    //MQTT
    const val MQTT_BROKER_ADDRESS = "tcp://broker.emqx.io:1883"
    const val MQTT_CLIENT_ID = "xget2323232"

    //
    const val CONNECTION_KEY = "CONNECTION_KEY"
    const val CONNECTED = "CONNECTEd"
    const val historyProperty = "history"
    const val ConnectionStatusProperty = "connectionStatus"
    const val empty = ""
    const val IsFirstTime = "IsFirstTime"


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
