package xget.dev.jet.presentation.main.history

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.presentation.main.home.HomeUiState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    preferences: SharedPreferences,
    private val savedStateHandle: SavedStateHandle,
    private val devicesService: DevicesRemoteService
) : BaseViewModel<HistoryState>() {


    val userId = preferences.getString(ConstantsShared.USER_ID, "") ?: ""
    val lastDevices: List<DeviceDto> = savedStateHandle["HISTORYVIEWMODELSTATE"] ?: emptyList()
    val lastQuantityOfDevices = preferences.getInt(ConstantsShared.LastQuantityOfDevices, 2)

    init {
        if (lastDevices.isEmpty()) {
            Log.d("devicesHistory", "First Fetch")
            fetchDevices()
        } else {
            Log.d("devicesHistory", "Cache devices Fetch")
            _state.update {
                it.copy(devices = lastDevices, isLoading = false)
            }
        }

    }

    private fun fetchDevices() {
        devicesService.getDevicesByUser().onEach { response ->
            val responseDevices = response.data?.myDevices ?: emptyList()
            _state.update {
                it.copy(devices = responseDevices, isError = response.errorMsg, isLoading = false)
            }
            savedStateHandle["HISTORYVIEWMODELSTATE"] = responseDevices
        }.launchIn(viewModelScope)
    }

    override fun defaultState(): HistoryState {
        return HistoryState(isLoading = true)
    }

    override fun onCleared() {
        super.onCleared()
        savedStateHandle["HISTORYVIEWMODELSTATE"] = state.value.devices
        Log.d("historu", "HistoryViewmodel on cleared")
    }

}
