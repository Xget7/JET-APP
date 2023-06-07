package xget.dev.jet.presentation.auth.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.data.remote.auth.dto.login.LoginRequest
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.repository.auth.AuthRepository
import xget.dev.jet.domain.repository.token.Token
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val token : Token
): BaseViewModel<LoginUiState>() {

    var userGmail by mutableStateOf("juan2@gmail.com")
        private set
    var userPassword by mutableStateOf("hola123")
        private set



    fun signInUser(){
        val request = LoginRequest(userGmail, userPassword)
        _uiState.update {
            _uiState.value.copy(isLoading = true)
        }

        viewModelScope.launch {

            when (val result = authRepository.login(request) ) {
                is ApiResponse.Error -> {
                    Log.d("apiResponseViewmodel", "error ${result.message}")
                    _uiState.update {
                        _uiState.value.copy(isError = result.message, isLoading = false)
                    }
                    delay(5000)
                    _uiState.update {
                        _uiState.value.copy(isError = null, isLoading = false)
                    }
                }

                is ApiResponse.Loading -> {
                    _uiState.update {
                        _uiState.value.copy(isLoading = true)
                    }
                }

                is ApiResponse.Success -> {
                    token.setJwtLocal(result.data?.jwt ?: "")
                    _uiState.update {
                        _uiState.value.copy(isLoggedIn = true, isLoading = false)
                    }
                }
            }

        }

    }



    fun updateUserGmail(input: String) {
        userGmail = input
    }

    fun updateUserPassword(input: String) {
        userPassword = input
    }

    override fun defaultState(): LoginUiState {
        return LoginUiState()
    }


}