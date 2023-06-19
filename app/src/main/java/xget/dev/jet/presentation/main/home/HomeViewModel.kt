package xget.dev.jet.presentation.main.home

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.core.utils.ConstantsShared.USER_ID
import xget.dev.jet.domain.repository.devices.DevicesRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val preferences : SharedPreferences,
    val devicesRepository: DevicesRepository
): BaseViewModel<HomeUiState>() {

    val userId  = mutableStateOf("")

    init {
        userId.value = preferences.getString(USER_ID,"") ?: ""
    }


    fun getDevicesState(){
        if (userId.value.isEmpty()){
            return
        }
        viewModelScope.launch {
            devicesRepository.getDevicesByUserId(userId.value)
                .onEach {

                }
        }

    }



    override fun defaultState(): HomeUiState {
        return HomeUiState()
    }
}