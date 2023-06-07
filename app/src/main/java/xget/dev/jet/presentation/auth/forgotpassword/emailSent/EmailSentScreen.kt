package xget.dev.jet.presentation.auth.forgotpassword.emailSent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
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
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.ui.theme.JetBlue
import xget.dev.jet.ui.theme.JetDarkBlue
import xget.dev.jet.ui.theme.JetMagenta


@Composable
internal fun EmailSentScreen(
    navController: NavController,
) {

    EmailSentScreen(
        goToLogin = { navController.navigate(Screens.LoginScreen.route) },
        goToSendEmailScreen = {
            navController.navigateUp()
        })
}

@Composable
internal fun EmailSentScreen(
    goToSendEmailScreen: () -> Unit,
    goToLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(16.dp)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Te enviamos un Email",
                fontSize = 25.sp,
                color = JetDarkBlue,
                fontWeight = FontWeight.Bold
            )
            TextWithShadow(
                text = "Por favor chequea tu  correo y clickea en el link" +
                        "que recibiste para cambiar la contraseña",
                modifier = Modifier,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                shadow = false,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(55.dp))

            Image(
                painter = painterResource(id = R.drawable.forgot_password_email_sended),
                contentDescription = "Image"
            )

            Spacer(modifier = Modifier.height(42.dp))



            Spacer(modifier = Modifier.height(60.dp))

            CustomBackgroundButton(
                "Ingresar",
                containerColor = JetMagenta,
                onClick = goToLogin
            )
            Spacer(modifier = Modifier.height(22.dp))
            Row() {

                TextWithShadow(
                    text = "¿No recibiste nada?",
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    shadow = false,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(11.dp))
                TextWithShadow(
                    text = "Reenviar",
                    modifier = Modifier.clickable {
                        goToSendEmailScreen()
                    },
                    fontWeight = FontWeight.Medium,
                    color = JetBlue,
                    fontSize = 18.sp,
                    shadow = false
                )
            }


        }
    }
}

@Preview
@Composable
fun SendedEmailScreenPrev() {
    EmailSentScreen({}, {})
}