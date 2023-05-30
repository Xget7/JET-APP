package xget.dev.jet.presentation.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.CustomTextButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue
import xget.dev.jet.ui.theme.JetDarkBlue

@Composable
internal fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {



}

@Composable
internal fun LoginScreen(
    userGmail: String,
    password: String,
    updateUserName: (String) -> Unit,
    updatePassword: (String) -> Unit,
    onLogin: () -> Unit,
    onPasswordReset: () -> Unit
) {

    val showPassword = remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.padding(16.dp)) {
        TopCustomBar("Ingresa a tu cuenta")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 50.dp, bottom = 0.dp),
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

            JetTextField(
                text = password,
                textLabel = "Contraseña",
                onValue = updatePassword,
                trailingIcon = {
                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            painter = painterResource(id = if (showPassword.value) R.drawable.password_eye_open else R.drawable.password_eye_close),
                            contentDescription = "Eye password",
                            modifier = Modifier.padding(2.dp),
                            tint = if (showPassword.value) JetBlue else Color.Gray
                        )
                    }
                }
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
                    modifier = Modifier.clickable { },
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(60.dp))

            CustomBackgroundButton(
                "Ingresar",
                onClick = { }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 27.dp),
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
                    .padding(top = 1.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextWithShadow(
                    text = "¡Registrate aca!",
                    color = JetBlue,
                    fontSize = 18.sp,
                    shadow = false,
                    modifier = Modifier.clickable { },
                    fontWeight = FontWeight.Medium
                )
            }

        }


    }


}

@Preview
@Composable
fun LoginScreenPreview() {
    JETTheme {
        LoginScreen(
            userGmail = "",
            password = "",
            updateUserName = {},
            updatePassword = {},
            onLogin = { /*TODO*/ }) {
        }
    }
}