package xget.dev.jet.presentation.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xget.dev.jet.core.base.BaseViewModel
import xget.dev.jet.domain.repository.auth.AuthRepository
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    val authRepository: AuthRepository
): BaseViewModel<LoginUiState>() {

    var userGmail by mutableStateOf("")
        private set
    var userPassword by mutableStateOf("")
        private set

    init {
//        TODO("Logica repositorio request y obtener tokens, verificar " +
//                "campos como password mas de 6 caracteres (queso) y email valido")

    }



    fun updateUserGmail(input: String) {
        userGmail = input
    }

    fun updateUserPassword(input: String) {
        userGmail = input
    }

    override fun defaultState(): LoginUiState {
        return LoginUiState()
    }


}