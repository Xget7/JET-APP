package xget.dev.jet.presentation.main.device_config.device_search

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnectionResult
import xget.dev.jet.domain.repository.bluetooth.BluetoothController
import javax.inject.Inject

@HiltViewModel
@SuppressLint("MissingPermission")
class DeviceSearchViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val bluetoothController: BluetoothController,
) : BaseViewModel<DeviceSearchUiState>() {


    //combine states
    override var state = combine(
        bluetoothController.pairedDevice,
        bluetoothController.scannedDevices,
        bluetoothController.isBluetoothOn,
        _state
    ) { pairedDevice, scannedDevices,bluetoothState, uiState ->
        uiState.copy(
            pairingDevice = pairedDevice != null,
            scannedDevices = scannedDevices ?: emptyList(),
            bluetoothOn = bluetoothState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    //Coroutine
    private var deviceConnectionJob: Job? = null
    var content = mutableIntStateOf(12220000)
    var counter: CountDownTimer? = null


    init {
        countDownDeviceSearch()
        startScan()

        bluetoothController.errors.onEach { error ->
            _state.update {
                it.copy(
                    errorMessage = error
                )
            }
        }.launchIn(viewModelScope)
        connectToDevice()
    }

    private fun countDownDeviceSearch() {
        viewModelScope.launch {
            delay(1000L)
            counter = object : CountDownTimer(120000, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    content.intValue = content.intValue - 1000
                }

                override fun onFinish() {
                    stopScan()
                }
            }.start()
        }
    }


    private fun connectToDevice() {
        bluetoothController.pairedDevice.onEach { device ->
            if (device != null){
                Log.d("DeviceSearchViewmodel", "connectToDevice try")
                Log.d("DeviceSearchViewmodel", "device to pair ${bluetoothController.pairedDevice.value?.name}")
                _state.update { it.copy(pairingDevice = true) }
                deviceConnectionJob = bluetoothController
                    .connectToDevice(bluetoothController.pairedDevice.value!!)
                    .listen()
            }else{
                Log.d("DeviceSearchViewmodel", "connectToDevice  == null")
                _state.update { it.copy(searchingDevice = true) }
            }
        }.launchIn(viewModelScope)


    }

    private fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                searchingDevice = false,
                pairingDevice = false,
            )
        }
    }

    private fun startScan() {
        bluetoothController.startDiscovery()

    }

    private fun stopScan() {
        bluetoothController.stopDiscovery()
    }


    fun pairDevice() {
        val messages = listOf("SSID:${null}", "PASSWORD:${null}", "USER_ID${null}")
        connectToDevice()
//        viewModelScope.launch {
//            for (e in messages) {
//                val bluetoothMessage = bluetoothController.trySendMessage(e)
//                if (bluetoothMessage != null) {
//
//                }
//            }
//
//        }
    }

    private fun Flow<BluetoothConnectionResult>.listen(): Job {
        return onEach { result ->
            Log.d("Listening Result", result.toString())
            when (result) {

                BluetoothConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            pairingDevice = true,
                            searchingDevice = false,
                            errorMessage = null
                        )
                    }
                }

                is BluetoothConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            pairingDevice = false,
                            searchingDevice = false,
                            errorMessage = result.message
                        )
                    }
                }

                is BluetoothConnectionResult.TransferSucceeded -> {
                    //received messages?
                }
            }
        }.catch { throwable ->
            Log.d("DeviceSearchViewMdoel", throwable.localizedMessage ?: throwable.message ?: "null error")
            disconnectFromDevice()
            _state.update {
                it.copy(
                    pairingDevice = false,
                    searchingDevice = false,
                    errorMessage = throwable.localizedMessage
                )
            }
        }.launchIn(viewModelScope)
    }


    override fun onCleared() {
        super.onCleared()
//        disconnectFromDevice()
//        bluetoothController.release()
    }

    override fun defaultState(): DeviceSearchUiState {
        return DeviceSearchUiState()
    }

}