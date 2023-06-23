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

    var userGmail by mutableStateOf("p@p.com")
        private set
    var userPassword by mutableStateOf("p")
        private set



    fun signInUser(){
        val request = LoginRequest(userGmail, userPassword)
        _state.update {
            _state.value.copy(isLoading = true)
        }
        viewModelScope.launch {
            val result = authRepository.login(request)
            when (result) {
                is ApiResponse.Error -> {
                    Log.d("apiResponseViewmodel", "error ${result.errorMsg}")
                    _state.update {
                        _state.value.copy(isError = result.errorMsg, isLoading = false)
                    }
                    delay(3000)
                    _state.update {
                        _state.value.copy(isError = null, isLoading = false)
                    }
                }

                is ApiResponse.Loading -> {
                    _state.update {
                        _state.value.copy(isLoading = true)
                    }
                }

                is ApiResponse.Success -> {
                    _state.update {
                        _state.value.copy(isLoggedIn = true, isLoading = false)
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