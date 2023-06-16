package xget.dev.jet.presentation.auth.register

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
import xget.dev.jet.presentation.theme.JetDarkBlue2


@Composable
 fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(state.value.successfulCreated){
        if (state.value.successfulCreated){
            val act = context as AuthActivity
            act.navigateToMain()
        }
    }


    RegisterScreen(
        uiState = state,
        userName = viewModel.userName,
        userPhone = viewModel.userPhone,
        userEmail = viewModel.userEmail,
        userPassword = viewModel.userPassword,
        userConfirmPassword = viewModel.userConfirmPassword,
        updateUserName = viewModel::updateUserName,
        updateUserPhone = viewModel::updateUserPhone,
        updateUserEmail = viewModel::updateUserEmail,
        updateUserPassword = viewModel::updateUserPassword,
        updateUserConfirmPassword = viewModel::updateUserConfirmPassword,
        onRegisterClick = viewModel::registerUser
    ) {
        navController.navigate(Screens.LoginScreen.route)
    }


}

@Composable
internal fun RegisterScreen(
    uiState: State<RegisterUiState>,
    userName: String,
    userPhone: String,
    userEmail: String,
    userPassword: String,
    userConfirmPassword: String,
    updateUserName: (String) -> Unit,
    updateUserPhone: (String) -> Unit,
    updateUserEmail: (String) -> Unit,
    updateUserPassword: (String) -> Unit,
    updateUserConfirmPassword: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    val showPassword = remember {
        mutableStateOf(false)
    }

    val showConfirmPassword = remember {
        mutableStateOf(false)
    }
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(top = 5.dp),
        scaffoldState = scaffoldState
    ) { it



        LaunchedEffect(uiState.value) {
            if (uiState.value.isError != null){
                scaffoldState.snackbarHostState.showSnackbar(
                    uiState.value.isError ?: "Error Inesperado",
                    duration = SnackbarDuration.Long,
                    actionLabel = "Ok"
                )
            }


        }


        Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            TopCustomBar(title ="Crea tu cuenta", showBack = false){

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .verticalScroll(rememberScrollState())
                    .drawBehind {
                        drawCircle(
                            color = JetDarkBlue2,
                            center = Offset(1300F, 2300F),
                            radius = 450f
                        )
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.register_screen_person),
                    contentDescription = "Image"
                )

                Spacer(modifier = Modifier.height(39.dp))

                JetTextField(
                    text = userName,
                    textLabel = "Nombre",
                    onValue = updateUserName
                )

                Spacer(modifier = Modifier.height(16.dp))


                JetTextField(
                    text = userEmail,
                    textLabel = "Email",
                    onValue = updateUserEmail
                )

                Spacer(modifier = Modifier.height(16.dp))

                JetTextField(
                    text = userPhone,
                    textLabel = "Celular",
                    onValue = updateUserPhone
                )



                Spacer(modifier = Modifier.height(16.dp))

                PasswordJetTextField(
                    text = userPassword,
                    textLabel = "Contraseña",
                    onValue = updateUserPassword,
                    trailingIcon = {
                        IconButton(onClick = { showPassword.value = !showPassword.value }) {
                            Icon(
                                painter = painterResource(
                                    id =
                                    if (showPassword.value) R.drawable.password_eye_open
                                    else R.drawable.password_eye_close
                                ),
                                contentDescription = "Eye password",
                                modifier = Modifier.padding(2.dp),
                                tint = if (showPassword.value) JetBlue else Color.Gray
                            )
                        }
                    },
                    visibility = showPassword,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordJetTextField(
                    text = userConfirmPassword,
                    textLabel = "Confirmar contraseña",
                    onValue = updateUserConfirmPassword,
                    trailingIcon = {
                        IconButton(onClick = {
                            showConfirmPassword.value = !showConfirmPassword.value
                        }) {
                            Icon(
                                painter = painterResource(
                                    id =
                                    if (showConfirmPassword.value) R.drawable.password_eye_open
                                    else R.drawable.password_eye_close
                                ),
                                contentDescription = "Eye password",
                                modifier = Modifier.padding(2.dp),
                                tint = if (showConfirmPassword.value) JetBlue else Color.Gray
                            )
                        }
                    },
                    visibility = showConfirmPassword,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)

                )

                Spacer(modifier = Modifier.height(31.dp))

                if (!uiState.value.isLoading){
                    CustomBackgroundButton(
                        "Registrarme",
                        onClick = onRegisterClick
                    )
                }else {
                   AlignedCircularProgressIndicator()
                }


                Spacer(modifier = Modifier.height(18.dp))

                TextWithShadow(
                    text = "¿Ya tenés cuenta?",
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    shadow = false,
                    color = Color.Gray
                )

                TextWithShadow(
                    text = "¡Ingresa aca!",
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .clickable {
                            onGoToLogin()
                        },
                    fontWeight = FontWeight.Medium,
                    shadow = false,
                    color = JetBlue,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(20.dp))


            }

        }


    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    JETTheme {
        RegisterScreen(
            uiState = mutableStateOf(RegisterUiState()),
            userName = "",
            userPhone = "",
            userEmail = "",
            userPassword = "",
            userConfirmPassword = "",
            updateUserName = {},
            updateUserPhone = {},
            updateUserEmail = {},
            updateUserPassword = {},
            updateUserConfirmPassword = {},
            {},
            {}
        )
    }

}