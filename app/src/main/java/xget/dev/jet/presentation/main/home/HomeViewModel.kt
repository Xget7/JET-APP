package xget.dev.jet.presentation.main.home

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared.LastQuantityOfDevices
import xget.dev.jet.core.utils.ConstantsShared.USER_ID
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.repository.devices.DevicesRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    val devices: DevicesRepository
) : BaseViewModel<HomeUiState>() {

    val userId = mutableStateOf("")
    val lastQuantityOfDevices = preferences.getInt(LastQuantityOfDevices, 2)

    private var maxRetry = 0


    init {
        //create fake device
        userId.value = preferences.getString(USER_ID, "") ?: ""
        devices.initRepo().onEach {
            Log.d("initRepo", it.toString() + userId.value)
            if (it) {
                getDevicesByUserId()
            } else {
                retryConnection()
            }
        }.launchIn(viewModelScope)
    }

    private fun retryConnection() {
        maxRetry++
        if (maxRetry > 3) {
            _state.update {
                it.copy(
                    isLoading = false,
                    isError = "Conexion Perdida, Reintentado conextarse..."
                )
            }
        }
    }


    private fun releaseAndUnSubscribe() {
        viewModelScope.launch {
            devices.releaseMqtt()
        }
        //implement and try connect whe ninternnet
    }

    override fun onCleared() {
        super.onCleared()
        preferences.edit().putInt(LastQuantityOfDevices, _state.value.myDevices.value.size).apply()
        releaseAndUnSubscribe()
    }

    private fun getDevicesByUserId() {
        devices.getDevicesByUserId(userId.value).onEach { smartDevices ->
            Log.d("viewmodelDevicesByUserid", smartDevices.toString())
            _state.update {
                it.copy(
                    myDevices = MutableStateFlow(smartDevices.filter { it.uid == userId.value }),
                    otherUsersDevices = MutableStateFlow(smartDevices.filter { it.uid != userId.value }),
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }


    fun sendMessageToDevice(device: SmartDevice) {
        viewModelScope.launch {
            devices.sendMessageToDevice(
                device.id,
                if (device.stateValue.intValue == 0) 1 else 0,
                device.uid
            )
        }
    }


    override fun defaultState(): HomeUiState {
        return HomeUiState(isLoading = true)
    }

}

