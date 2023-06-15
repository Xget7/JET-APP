package xget.dev.jet.core.utils

import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.IntentSender
import android.location.LocationManager

import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

class WifiUtil(val context: Context) {
    fun getWifiSSID(): String {
        val mWifiManager: WifiManager =
            (context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
        val info: WifiInfo = mWifiManager.connectionInfo

        return if (info.supplicantState === SupplicantState.COMPLETED) {
            val ssid: String = info.ssid
            ssid.substring(1,ssid.lastIndex )
        } else {
            Log.d("wifi name", "could not obtain the wifi name")
            "Error obteniendo nombre del wifi."
        }
    }

}

fun checkLocationSetting(
    activty: Activity,
    onDisabled: () -> Unit,
    onEnabled: () -> Unit
) {

    val locationManager: LocationManager = activty.getSystemService(LocationManager::class.java)

    val isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    if (isEnabled) {
        onEnabled()
    } else {
        onDisabled()
    }

}