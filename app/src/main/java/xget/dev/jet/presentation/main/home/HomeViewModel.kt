package xget.dev.jet.presentation.main.home

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared.USER_ID
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferences : SharedPreferences,
    val mqttClient: MqttFlowClient
): BaseViewModel<HomeUiState>() {

    val userId  = mutableStateOf("")
    var subscirbed = false



    init {
//        mqttClient.startMqttService()
//        userId.value = preferences.getString(USER_ID,"") ?: ""
//
//        mqttClient.connectionStatus.onEach {
//            Log.d("mqttClientstate", it.toString())
//            if (it && !subscirbed){
//                viewModelScope.launch {
//                    mqttClient.subscribe("389283928392/2")
//                    subscirbed = true
//
//                    mqttClient.receiveMessages().onEach {
//                        Log.d("MqttMessages", it.toString())
//
//                    }.launchIn(this)
//                }
//            }
//        }.launchIn(viewModelScope)
    }






    fun getDevicesState(){
        if (userId.value.isEmpty()){
            return
        }


    }



    override fun defaultState(): HomeUiState {
        return HomeUiState()
    }
}