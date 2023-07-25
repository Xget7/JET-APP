package xget.dev.jet.presentation.main.history.device_history

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import javax.inject.Inject

@HiltViewModel
class DeviceHistoryViewModel @Inject constructor(
    preferences: SharedPreferences,
    savedStateHandle: SavedStateHandle,
    private val devicesService: DevicesRemoteService
) : BaseViewModel<DeviceHistoryState>() {


    val userId = preferences.getString(ConstantsShared.USER_ID, "") ?: ""
    val deviceId: String = savedStateHandle["deviceId"] ?: ""
    private val deviceName: String = savedStateHandle["deviceName"] ?: ""

    init {
        fetchDeviceData()
    }

    private fun fetchDeviceData() {
        Log.d("fetchDeviceHistory", "device id $deviceId and name $deviceName")
        _state.update {
            it.copy( deviceName = deviceName)
        }
        fetchDeviceHistory()
    }

    private fun fetchDeviceHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val historyResponse = devicesService.getDeviceHistory(deviceId)
                // Handle the ApiResponse.Success response
                if (historyResponse is ApiResponse.Success) {
                    _state.update {
                        it.copy(isLoading = false, history = historyResponse.data?.history!!.reversed())
                    }
                } else if (historyResponse is ApiResponse.Error) {
                    // Handle the error case
                    val errorMessage = historyResponse.errorMsg
                    _state.update {
                        it.copy(isLoading = false, isError = errorMessage)
                    }

                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, isError = "Ocurrio un error obteniendo el historial")
                }
            }
        }

    }


    override fun defaultState(): DeviceHistoryState {
        return DeviceHistoryState(isLoading = true)
    }

}
