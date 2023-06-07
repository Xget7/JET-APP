package xget.dev.jet.presentation.auth.register

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.data.util.network.ApiResponse
import xget.dev.jet.domain.model.user.RegisterUser
import xget.dev.jet.domain.repository.user.UserRepository
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<RegisterUiState>() {

    var userName by mutableStateOf("random")
        private set
    var userEmail by mutableStateOf("random@gmail.com")
        private set
    var userPhone by mutableStateOf("113409234")
        private set
    var userPassword by mutableStateOf("hola12341")
        private set
    var userConfirmPassword by mutableStateOf("hola12341")
        private set


    var currentUser = RegisterUser()


    private val userHasLocalError by derivedStateOf {
        // synchronous call
        currentUser.name = userName
        currentUser.email = userEmail
        currentUser.phoneNumber = userPhone
        currentUser.password = userPassword
        currentUser.confirmPassword = userConfirmPassword
        userRepository.isValidUser(currentUser)
    }

    fun registerUser() {
        Log.d("registerHasLocalEror", userHasLocalError.second.toString())
        if (!userHasLocalError.second) {
            _uiState.update {
                _uiState.value.copy(isError = userHasLocalError.first, isLoading = false)
            }
            return
        }
        _uiState.update {
            _uiState.value.copy(isLoading = true)
        }

        viewModelScope.launch {
            when (val result = userRepository.registerUser(currentUser.toUserRequest())) {
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
                    _uiState.update {
                        _uiState.value.copy(successfulCreated = true, isLoading = false)
                    }
                }

                null -> {
                    _uiState.update {
                        _uiState.value.copy(isError = "Error inesperado.", isLoading = false)
                    }
                }
            }
        }
    }


    fun updateUserName(name: String) {
        userName = name
    }

    fun updateUserPhone(phone: String) {
        userPhone = phone
    }

    fun updateUserEmail(email: String) {
        userEmail = email
    }

    fun updateUserPassword(password: String) {
        userPassword = password
    }

    fun updateUserConfirmPassword(confirmPassword: String) {
        userConfirmPassword = confirmPassword
    }


    override fun defaultState(): RegisterUiState {
        return RegisterUiState()
    }


}