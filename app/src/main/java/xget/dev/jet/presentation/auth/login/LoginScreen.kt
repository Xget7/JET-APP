package xget.dev.jet.presentation.auth.login

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {

}

@Composable
internal fun LoginScreen(
    onLogin : () -> Unit,
    onPasswordReset : () -> Unit
){

}