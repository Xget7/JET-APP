package xget.dev.jet.presentation.auth.forgotpassword.sendEmailVerification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue
import xget.dev.jet.ui.theme.JetDarkBlue
import xget.dev.jet.ui.theme.JetMagenta


@Composable
internal fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgoPasswordViewModel = hiltViewModel()
) {
    ForgotPasswordScreen(
        userEmail = viewModel.userEmail,
        updateUserEmail = viewModel::updateEmailField,
        sendEmail = viewModel::sendEmail,
        login = { navController.navigateUp() },
    )

}

@Composable
internal fun ForgotPasswordScreen(
    userEmail: String,
    updateUserEmail: (String) -> Unit,
    sendEmail: () -> Unit,
    login: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopCustomBar("", onClick = login)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 26.sp,
                color = JetDarkBlue,
                fontWeight = FontWeight.Bold
            )
            TextWithShadow(
                text = "No te preocupes , te enviaremos\n" +
                        "un email para resetearla",
                modifier = Modifier,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                shadow = false,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(42.dp))

            Image(
                painter = painterResource(id = R.drawable.forgot_password_person),
                contentDescription = "Image"
            )

            Spacer(modifier = Modifier.height(42.dp))

            JetTextField(
                text = userEmail,
                textLabel = "Email",
                onValue = updateUserEmail
            )

            Row(
                Modifier
                    .padding(top = 17.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                TextWithShadow(
                    text = "¿Te la acordaste?",
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    shadow = false,
                    color = Color.Gray,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                TextWithShadow(
                    text = "Ingresá",
                    modifier = Modifier.clickable {
                        login()
                    },
                    fontWeight = FontWeight.Medium,
                    color = JetBlue,
                    fontSize = 17.sp,
                    shadow = false
                )
            }

            Spacer(modifier = Modifier.height(80.dp))

            CustomBackgroundButton(
                "Enviar Correo",
                containerColor = JetMagenta,
                onClick = sendEmail
            )

        }
    }

}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    JETTheme {
        ForgotPasswordScreen(userEmail = "",
            updateUserEmail = {}, {}) {}
    }

}