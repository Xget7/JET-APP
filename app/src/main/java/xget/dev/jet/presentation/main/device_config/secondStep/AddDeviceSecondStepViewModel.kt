package xget.dev.jet.presentation.main.device_config.secondStep

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared.LAST_DEVICE_SELECTED
import xget.dev.jet.core.utils.ConstantsShared.LAST_WIFI_SSID
import xget.dev.jet.core.utils.ConstantsShared.WIFI_CREDENTIALS
import xget.dev.jet.core.utils.WifiUtil
import javax.inject.Inject

@HiltViewModel
class AddDeviceSecondStepViewModel @Inject constructor(
    private val wifiUtil: WifiUtil,
    private val prefs: SharedPreferences
) : BaseViewModel<AddDeviceSecondStepUiState>() {


    var wifiSsid by mutableStateOf("")
        private set
    var wifiPassword by mutableStateOf("")
        private set


    fun changeWifiSSid(ssid: String){
        wifiSsid = ssid
    }

    fun changeWifiPassword(ssid: String){
        wifiPassword = ssid
    }

    override fun defaultState(): AddDeviceSecondStepUiState {
        return AddDeviceSecondStepUiState()
    }

    fun saveWifiCredentials() {
        prefs.edit().putStringSet(WIFI_CREDENTIALS, setOf(wifiSsid,wifiPassword)).apply()
    }

    fun getWifiSsid() {
        wifiSsid = wifiUtil.getWifiSSID()
    }
}