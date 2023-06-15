package xget.dev.jet.presentation.auth.forgotpassword.sendEmailVerification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.data.remote.auth.dto.forgotPass.ForgotPasswordRequest
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.repository.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ForgoPasswordViewModel @Inject constructor(
    val authRepository: AuthRepository
) : BaseViewModel<ForgotPasswordUiState>() {

    var userEmail by mutableStateOf("")


    fun updateEmailField(i: String) {
        userEmail = i
    }

    fun sendEmail() {
        viewModelScope.launch {
            when (val result =
                authRepository.sendForgotPasswordEmail(ForgotPasswordRequest(userEmail))
            ){
                is ApiResponse.Error -> {
                    _state.update {
                        _state.value.copy(isError = result.message, isLoading = false)
                    }
                    delay(2000)
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
                        _state.value.copy(emailSent = true, isLoading = false)
                    }
                }
            }
        }


    }

    override fun defaultState(): ForgotPasswordUiState {
        return ForgotPasswordUiState()
    }
}