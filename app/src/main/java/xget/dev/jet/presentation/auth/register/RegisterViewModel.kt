package xget.dev.jet.presentation.auth.register

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.data.remote.users.dto.UserRequest
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.model.user.RegisterUser
import xget.dev.jet.domain.repository.user.UserRepository
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<RegisterUiState>() {

    var user by mutableStateOf(RegisterUser())
        private set


    private val userHasLocalError by derivedStateOf {
        // synchronous call
        userRepository.isValidUser(user)
    }

    fun registerUser() {
        viewModelScope.launch {
            if (userHasLocalError.second) {
                _uiState.update {
                    _uiState.value.copy(isError = userHasLocalError.first, isLoading = false)
                }
                return@launch
            }

            when (val result = userRepository.registerUser(user.toUserRequest())) {
                is ApiResponse.Error -> {
                    _uiState.update {
                        _uiState.value.copy(isError = result.message, isLoading = false)
                    }
                }

                is ApiResponse.Loading -> {
                    _uiState.update {
                        _uiState.value.copy(isLoading = true)
                    }
                }

                is ApiResponse.Success -> {
                    _uiState.value.copy(successfulCreated = true, isLoading = false)
                }

                null -> {
                    _uiState.update {
                        _uiState.value.copy(isError = "Error inesperado.", isLoading = false)
                    }
                }
            }
        }
    }

    fun updateUserName(name : String) {
        user.name = name
    }


    fun updateUserPhone(phone: String) {
        user.phoneNumber = phone
    }

    fun updateUserEmail(email: String) {
        user.gmail = email
    }

    fun updateUserPassword(password: String) {
        user.password = password
    }

    fun updateUserConfirmPassword(confirmPassword: String) {
        user.confirmPassword = confirmPassword
    }


    override fun defaultState(): RegisterUiState {
        return RegisterUiState()
    }


}