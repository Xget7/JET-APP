package xget.dev.jet.presentation.main.home.device_details

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.repository.devices.DevicesRepository
import javax.inject.Inject

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val devicesRepo: DevicesRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<DeviceDetailUiState>() {
    private val deviceId: String? = savedStateHandle.get<String>("deviceId")
    private val userDeviceId: String? = savedStateHandle.get<String>("userDeviceId")

    init {
        if (deviceId != null) {
            getDevice()

        }

    }

    private fun getDevice() {
        viewModelScope.launch {
            devicesRepo.sendMessageToDevice(
                deviceId!!,
                3,
                userDeviceId!!
            )
        }
        devicesRepo.getDevice(deviceId!!, userDeviceId!!).onEach { smartDevice ->
            Log.d("SoloDevice", "update SOlo Device ${smartDevice}")
            _state.update {
                it.copy(smartDevice = smartDevice, isLoading = false)
            }
        }.catch { throwable ->
            _state.update {
                it.copy(isLoading = false, msgError = throwable.message)
            }
        }.launchIn(viewModelScope)
    }


    fun sendMessageToDevice() {
        viewModelScope.launch {
            devicesRepo.sendMessageToDevice(
                deviceId!!,
                if (_state.value.smartDevice.stateValue.intValue == 0) 1 else 0, userDeviceId!!
            )
        }
    }

    override fun defaultState(): DeviceDetailUiState {
        return DeviceDetailUiState(isLoading = true)
    }

}