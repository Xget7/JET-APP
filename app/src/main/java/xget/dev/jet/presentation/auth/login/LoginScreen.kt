package xget.dev.jet.presentation.auth.login

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.AlignedCircularProgressIndicator
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.PasswordJetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.presentation.auth.AuthActivity
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetBlue

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()


    LoginScreen(
        uiState = state,
        userGmail = viewModel.userGmail,
        password = viewModel.userPassword,
        updateUserName = viewModel::updateUserGmail,
        updatePassword = viewModel::updateUserPassword,
        onLogin = viewModel::signInUser,
        onPasswordReset = {
            navController.navigate(Screens.ForgotPasswordScreen.route)
        },
        goToRegister = {
            navController.navigate(Screens.RegisterScreen.route)
        }
    )


}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun LoginScreen(
    uiState: State<LoginUiState>,
    userGmail: String,
    password: String,
    updateUserName: (String) -> Unit,
    updatePassword: (String) -> Unit,
    onLogin: () -> Unit,
    goToRegister: () -> Unit,
    onPasswordReset: () -> Unit
) {

    val showPassword = remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current


    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()
    val focUsManager = LocalFocusManager.current
    val bringIntoViewRequester = BringIntoViewRequester()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background( Color.White)
            .padding(start = 1.dp, end = 1.dp, top = 12.dp ),
        topBar = {
            TopCustomBar(title = "Ingresa a tu cuenta", showBack = false, onClick = {})
        },
        backgroundColor =  Color.White
    ) {
        it
        val act = LocalContext.current
        LaunchedEffect(uiState.value) {
            if (uiState.value.isError != null) {
                keyboardController?.hide()
                scaffoldState.snackbarHostState.showSnackbar(
                    uiState.value.isError ?: "Error Inesperado.",
                    duration = SnackbarDuration.Long,
                    actionLabel = "Ok"
                )
            }
            if (uiState.value.isLoggedIn) {
                val a = act as AuthActivity
                a.navigateToMain()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 30.dp, bottom = 0.dp , start = 16.dp, end = 16.dp,)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.login_screen_image),
                contentDescription = "Image"
            )

            JetTextField(
                text = userGmail,
                "Email",
                onValue = updateUserName
            )

            Spacer(modifier = Modifier.height(21.dp))

            PasswordJetTextField(
                text = password,
                textLabel = "Contraseña",
                onValue = updatePassword,
                modifier = Modifier.onFocusEvent {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            painter = painterResource(id = if (showPassword.value) R.drawable.password_eye_open else R.drawable.password_eye_close),
                            contentDescription = "Eye password",
                            modifier = Modifier.padding(2.dp),
                            tint = if (showPassword.value) JetBlue else Color.Gray
                        )
                    }
                },
                visibility = showPassword,
                keyboardActions = KeyboardActions(
                    onDone = { focUsManager.clearFocus() }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, end = 20.dp), horizontalArrangement = Arrangement.End
            ) {
                TextWithShadow(
                    text = "Olvidaste la contraseña?",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    shadow = false,
                    modifier = Modifier.clickable {
                        onPasswordReset()
                    },
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(60.dp))

            if (!uiState.value.isLoading) {
                CustomBackgroundButton(
                    "Ingresar",
                    onClick = onLogin,
                    modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)
                )
            } else {
                keyboardController?.hide()
                AlignedCircularProgressIndicator()
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextWithShadow(
                    text = "¿Todavia no tenes cuenta?",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    shadow = false,
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextWithShadow(
                    text = "¡Registrate aca!",
                    color = JetBlue,
                    fontSize = 18.sp,
                    shadow = false,
                    modifier = Modifier.clickable {
                        goToRegister()
                    },
                    fontWeight = FontWeight.Medium
                )
            }

        }


    }


}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun LoginScreenPreview() {
    JETTheme {
        LoginScreen(
            uiState = mutableStateOf(LoginUiState()),
            userGmail = "",
            password = "",
            updateUserName = {},
            updatePassword = {},
            onLogin = { /*TODO*/ }, {}) {
        }
    }
}