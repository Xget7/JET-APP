package xget.dev.jet.presentation.auth.forgotpassword.sendEmailVerification

import android.content.res.Resources.Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue
import xget.dev.jet.ui.theme.JetDarkBlue

@Composable
internal fun ForgotPasswordScreen(
    userEmail: String,
    updateUserEmail: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TopCustomBar("")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 26.sp,
                color = JetDarkBlue,
                fontWeight = FontWeight.Medium
            )
            TextWithShadow(
                text = "No te preocupes , te enviaremos\n" +
                        "un email para resetearla",
                modifier = Modifier,
                fontWeight = FontWeight.Medium,
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
                text = "",
                textLabel = "Email",
                onValue = updateUserEmail
            )

            Row(
                Modifier
                    .padding(top = 17.dp)
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

                TextWithShadow(text = "Ingresá",
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    color = JetBlue,
                    fontSize = 18.sp,
                    shadow = false)
            }

            Spacer(modifier = Modifier.height(80.dp))

            CustomBackgroundButton ("Enviar correo",
                onClick = {})

        }
    }

}

@Preview
@Composable
fun ForgotPasswordScreenPreview() {
    JETTheme {
        ForgotPasswordScreen(userEmail = "",
            updateUserEmail = {})
    }

}