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
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.core.utils.ConstantsShared.USER_ID
import xget.dev.jet.core.utils.ConstantsShared.WIFI_CREDENTIALS
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
    ) { pairedDevice, scannedDevices, bluetoothState, uiState ->
        uiState.copy(
            pairedDevice = pairedDevice,
            scannedDevices = scannedDevices ?: emptyList(),
            bluetoothOn = bluetoothState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    //Coroutine
    private var deviceConnectionJob: Job? = null
    var content = mutableIntStateOf(120000)
    var counter: CountDownTimer? = null

    val userId = sharedPreferences.getString(USER_ID, "")

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
            if (device != null && device.name.contains("JET")) {
                Log.d("DeviceSearchViewmodel", "connectToDevice try")
                Log.d(
                    "DeviceSearchViewmodel",
                    "device to pair ${bluetoothController.pairedDevice.value?.name}"
                )
                _state.update { it.copy(pairingDevice = true, searchingDevice = false) }
                deviceConnectionJob = bluetoothController
                    .connectToDevice()
                    .listen()
                Log.d("DeviceSearchViewmodel", "Listen atacched ")

                return@onEach
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


    suspend fun sendMessages() {
        Log.d("sendMessageFun", " executed")
//        val wifiCredentials = sharedPreferences.getStringSet(WIFI_CREDENTIALS, setOf())?.toList()
//
//        val messages = listOf(
//            "SSID:${wifiCredentials?.get(0)}\n",
//            "PASSWORD:${wifiCredentials?.get(1)}\n",
//            "USER_ID${userId}\n"
//        )

        val messages2 = listOf(
            "SSID:lauchita\n",
            "PASSWORD:lauchita\n",
            "USER_ID:lauchfd8f7d89fita\n"
        )

            for (message in messages2) {
                Log.d("sendMessageFun", " $message")

                bluetoothController.trySendMessage(message)
                delay(100)
            }
            _state.update {
                it.copy(searchingDevice = false, pairingDevice = false, syncingWithCloud = true)
            }

    }

    private fun Flow<BluetoothConnectionResult>.listen(): Job {
        return onEach { result ->
            Log.d("Listening Result", result.toString())
            when (result) {
                BluetoothConnectionResult.ConnectionEstablished -> {
                    Log.d("ConnectionStablished", "Yes")
                    _state.update {
                        it.copy(
                            pairingDevice = true,
                            searchingDevice = false,
                            errorMessage = null
                        )
                    }
                    sendMessages()
                }

                is BluetoothConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            pairingDevice = false,
                            searchingDevice = false,
                            errorMessage = result.errorMsg
                        )

                    }
                }

                is BluetoothConnectionResult.TransferSucceeded -> {
                    //received messages?
                    Log.d("Received Bluetooth MEssage", result.message)

                }
            }
        }.catch { throwable ->
            Log.d(
                "DeviceSearchViewMdoel",
                throwable.localizedMessage ?: throwable.message ?: "null error"
            )
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
        disconnectFromDevice()
        bluetoothController.release()
    }

    override fun defaultState(): DeviceSearchUiState {
        return DeviceSearchUiState()
    }

}