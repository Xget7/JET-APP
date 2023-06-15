package xget.dev.jet.presentation.main.device_config.firstStep

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared.LAST_DEVICE_SELECTED
import xget.dev.jet.data.util.location.LocationHelper
import xget.dev.jet.domain.repository.bluetooth.BluetoothController
import javax.inject.Inject

@HiltViewModel
class AddDeviceFirstStepViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    val bluetoothController: BluetoothController,
    val locationHelper: LocationHelper,
) : BaseViewModel<AddDeviceFirstStepUiState>() {
    private val lastDeviceSelected = sharedPreferences.getString(LAST_DEVICE_SELECTED, "Alarma")


    override var state = combine(
        bluetoothController.isBluetoothOn,
        _state
    ) { isBluetoothOn, uiState ->
        uiState.copy(
            bluetoothOn = isBluetoothOn,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    init {
        _state.update {
            Log.d("updatedStateBluetooth", bluetoothController.isBluetoothOn.value.toString())
            it.copy(bluetoothOn = bluetoothController.isBluetoothOn.value)
        }
    }


    fun tryEnableLocation() {
        viewModelScope.launch {
            val result = locationHelper
                .checkLocationSettings()

            _state.update {
                it.copy(
                    gpsOn =result.enabled,
                    intentSenderForResult = result.intentSenderForResult
                )
            }
        }

    }


    fun changeSelectedDevice(name: String) {
        _state.update {
            it.copy(selectedItem = name)
        }
        sharedPreferences.edit().putString(LAST_DEVICE_SELECTED, name).apply()
    }


    override fun defaultState(): AddDeviceFirstStepUiState {
        return AddDeviceFirstStepUiState(selectedItem = lastDeviceSelected ?: "Alarma")
    }
}