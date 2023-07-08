package xget.dev.jet.presentation.main.profile

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
import xget.dev.jet.core.utils.ConstantsShared.USER_ID
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.repository.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferences: SharedPreferences,
    val userRepository: UserRepository
) : BaseViewModel<ProfileUiState>() {

    private var maxRetry = 0
    val errorFetchingUser = "Error obteniendo usuario, reintentalo mas tarde."

    init {
        //create fake device
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            val response = userRepository.getUser()
            when (response) {
                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(isError = errorFetchingUser, isLoading = false)
                    }
                }
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    if (response.data != null) {
                        Log.d("response","reponse in viewmode ${response.data}")
                        _state.update {
                            it.copy(user = response.data, isLoading = false)
                        }
                    } else {
                        _state.update {
                            it.copy(isError = errorFetchingUser, isLoading = false)
                        }
                    }

                }
                else -> {
                    _state.update {
                        it.copy(isError = errorFetchingUser, isLoading = false)
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }

    override fun defaultState(): ProfileUiState {
        return ProfileUiState(isLoading = true)
    }
}

