package xget.dev.jet.presentation.main.device_config.device_search

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
import xget.dev.jet.core.utils.secondsToMinutes
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.repository.bluetooth.BluetoothConnectionResult
import xget.dev.jet.domain.repository.bluetooth.BluetoothController
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import javax.inject.Inject

@HiltViewModel
@SuppressLint("MissingPermission")
class DeviceSearchViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val bluetoothController: BluetoothController,
    private val deviceService: DevicesRemoteService,
) : BaseViewModel<DeviceSearchUiState>() {

    private var deviceConnectionJob: Job? = null
    var counter: CountDownTimer? = null

    val userId = sharedPreferences.getString(USER_ID, "")

    var content = MutableStateFlow(120000L)
    val currentDeviceId = mutableStateOf("")
    val creatingDevice = mutableStateOf(false)
    private fun countDownDeviceSearch() {
        viewModelScope.launch {
            delay(1000L)
            counter = object : CountDownTimer(120000, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    content.value = content.value - 1000
                }
                override fun onFinish() {
                    stopScan()
                }
            }.start()
        }
    }


    //combine states
    override var state = combine(
        bluetoothController.pairedDevice,
        bluetoothController.isBluetoothOn,
        _state,
        content,
    ) { pairedDevice, bluetoothState, uiState, time ->
        uiState.copy(
            pairedDevice = pairedDevice,
            bluetoothOn = bluetoothState,
            timeUntilStop = secondsToMinutes(time)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    //Coroutine

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


    private fun connectToDevice() {
        bluetoothController.pairedDevice.onEach { device ->
            if (device != null && device.name.contains("JET")) {
                Log.d("DeviceSearchViewmodel", "connectToDevice try")
                Log.d(
                    "DeviceSearchViewmodel",
                    "device to pair ${bluetoothController.pairedDevice.value?.name}"
                )
                delay(2000)
                _state.update { it.copy(pairingDevice = true, searchingDevice = false) }
                deviceConnectionJob = bluetoothController
                    .connectToDevice()
                    .listen()
                delay(1000)
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
        _state.update {
            it.copy(
                cantFindDevice = false
            )
        }
        bluetoothController.startDiscovery()

    }

    private fun stopScan() {
        bluetoothController.stopDiscovery()
        if (!_state.value.pairingDevice) {
            _state.update {
                it.copy(cantFindDevice = true)
            }
        }
    }


    private suspend fun sendMessages() {

        val wifiCredentials = sharedPreferences.getStringSet(WIFI_CREDENTIALS, setOf())?.toList()
        val messages = listOf(
            "SSID:${wifiCredentials?.get(0)}\n",
            "PASSWORD:${wifiCredentials?.get(1)}\n",
            "USER_ID${userId}\n"
        )
        for (message in messages) {
            Log.d("sendMessageFun", " $message")
            bluetoothController.trySendMessage(message)
            delay(100)
        }
        _state.update {
            it.copy(searchingDevice = false, pairingDevice = false, syncingWithCloud = true)
        }
    }

    private fun createDevice() {
        creatingDevice.value = true
        val deviceType = sharedPreferences.getString(ConstantsShared.LAST_DEVICE_SELECTED, "GATE")
        val deviceName = sharedPreferences.getString(ConstantsShared.LAST_DEVICE_NAME, "Dispositivo")
        currentDeviceId.value =  currentDeviceId.value.replace(":", "2")
        val newDevice = DeviceDto(
            id = currentDeviceId.value,
            name = deviceName ?: "Dispositivo",
            userId = userId.orEmpty(),
            deviceType = deviceType ?: "GATE",
            accessUsersIds = listOf(userId.orEmpty())
        )

        deviceService.createDevice(newDevice).onEach { response ->
            when (response) {
                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(errorMessage = response.errorMsg)
                    }
                }

                is ApiResponse.Loading -> {
                    Log.d("apiResponse", "LOADING????")

                }

                is ApiResponse.Success -> {
                    Log.d("apiResponse", "successo")
                    _state.update {
                        it.copy(finished = true)
                    }
                }
            }
        }.launchIn(viewModelScope)

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
                    counter?.cancel()
                    currentDeviceId.value = bluetoothController.pairedDevice.value?.address.toString()
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
                    Log.d("Received Bluetooth MEssage", result.message)
                    delay(2000)
                    if (!creatingDevice.value) {
                        createDevice()
                    }

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

    fun retry() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    errorMessage = null,
                    searchingDevice = false,
                    pairedDevice = null,
                    pairingDevice = false
                )
            }
            disconnectFromDevice()
            bluetoothController.release()

            delay(2000)

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

    }

}