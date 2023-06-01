package xget.dev.jet.presentation.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue


//internal fun Screens.RegisterScreen(
//    navController: NavController,
//    viewModel: RegisterViewModel: hilViewModel()
//){
//
//
//}

@Composable
internal fun RegisterScreen(
    userName: String,
    userPhone: String,
    userEmail: String,
    userPassword: String,
    userConfirmPassword: String,
    updateUserName: (String) -> Unit,
    updateUserPhone: (String) -> Unit,
    updateUserEmail: (String) -> Unit,
    updateUserPassword: (String) -> Unit,
    updateUserConfirmPassword: (String) -> Unit
) {
    val showPassword = remember {
        mutableStateOf(false)
    }

    val showConfirmPassword = remember{
        mutableStateOf(false)
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {

        TopCustomBar("Crea tu cuenta")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 86.dp, bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.register_screen_person),
                contentDescription = "Image"
            )

            Spacer(modifier = Modifier.height(39.dp))

            JetTextField(text = "",
                textLabel = "Nombre",
                onValue = updateUserName)

            Spacer(modifier = Modifier.height(21.dp))

            JetTextField(text = "",
                textLabel = "Celular",
                onValue = updateUserPhone)

            Spacer(modifier = Modifier.height(21.dp))


            JetTextField(text = "",
                textLabel = "Email",
                onValue = updateUserEmail)

            Spacer(modifier = Modifier.height(21.dp))

            JetTextField(text = "",
                textLabel = "Contraseña",
                onValue = updateUserPassword,
                trailingIcon = {
                    IconButton(onClick = { showPassword.value = !showPassword.value }) {
                        Icon(
                            painter = painterResource(id =
                            if (showPassword.value) R.drawable.password_eye_open
                            else R.drawable.password_eye_close),
                            contentDescription = "Eye password",
                            modifier = Modifier.padding(2.dp),
                            tint = if (showPassword.value) JetBlue else Color.Gray
                        )
                    }
                })

            Spacer(modifier = Modifier.height(21.dp))

            JetTextField(text = "",
                textLabel = "Confirmar contraseña",
                onValue = updateUserConfirmPassword,
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword.value = !showConfirmPassword.value }) {
                        Icon(
                            painter = painterResource(id =
                            if (showConfirmPassword.value) R.drawable.password_eye_open
                            else R.drawable.password_eye_close),
                            contentDescription = "Eye password",
                            modifier = Modifier.padding(2.dp),
                            tint = if (showConfirmPassword.value) JetBlue else Color.Gray
                        )
                    }
                })

            Spacer(modifier = Modifier.height(31.dp))
            
            CustomBackgroundButton(
                "Registrarme",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(18.dp))

            TextWithShadow(text = "¿Ya tenés cuenta?",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                shadow = false,
                color = Color.Gray
            )

            TextWithShadow(text = "¡Ingresa aca!",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .clickable { },
                fontWeight = FontWeight.Medium,
                shadow = false,
                color = JetBlue,
                fontSize = 18.sp
                )





        }


    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    JETTheme {
        RegisterScreen(
            userName = "",
            userPhone = "",
            userEmail = "",
            userPassword = "",
            userConfirmPassword = "",
            updateUserName = {},
            updateUserPhone = {},
            updateUserEmail = {},
            updateUserPassword = {},
            updateUserConfirmPassword = {}
        )
    }

}