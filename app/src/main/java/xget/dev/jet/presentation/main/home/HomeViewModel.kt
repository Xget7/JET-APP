package xget.dev.jet.presentation.main.home

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared.USER_ID
import xget.dev.jet.core.utils.secondsToMinutes
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    val devices: DevicesRepository
) : BaseViewModel<HomeUiState>() {

    val userId = mutableStateOf("")
    var subscirbed = false


    init {
        //create fake device
        userId.value = preferences.getString(USER_ID, "") ?: ""
//        _state.update {
//            it.copy(
//                devices = mutableListOf(
//                    SmartDevice(
//                        "7821849C1782",
//                        userId.value,
//                        "Porton casa",
//                        online = true
//                    )
//                )
//            )
//        }

//        devices.getDevicesByUserId(userId.value).onEach { smartDevices ->
//            Log.d("viewmodelDevicesByUserid", smartDevices.toString())
//            _state.update {
//                it.copy(devices = smartDevices)
//            }
//        }.launchIn(viewModelScope)


    }


    fun sendMessage(device: SmartDevice) {

    }


    fun getDevicesState() {
        if (userId.value.isEmpty()) {
            return
        }


    }


    override fun defaultState(): HomeUiState {
        return HomeUiState()
    }
}

