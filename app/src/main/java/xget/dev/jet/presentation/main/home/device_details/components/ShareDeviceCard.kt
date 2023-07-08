package xget.dev.jet.presentation.main.home.device_details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetBlue2
import xget.dev.jet.presentation.theme.JetGray2

@Composable
fun ShareDeviceDialog(
    onReady: (String) -> Unit,
    dismissRequest : () -> Unit
) {

    val email = remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = dismissRequest, properties = DialogProperties()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))

        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextWithShadow(
                    text = "Compartir Dispostivo",
                    modifier = Modifier,
                    fontWeight = FontWeight.Bold,
                    shadow = false,
                    fontSize = 22.sp,
                    color = JetBlue2
                )
                TextWithShadow(
                    text = "El usuario podra acceder a tu dispositivo , leer su estado y tambien cambiarlo.",
                    modifier = Modifier,
                    fontWeight = FontWeight.SemiBold,
                    shadow = false,
                    color = JetGray2
                )
                Spacer(modifier = Modifier.height(25.dp))

                JetTextField(
                    text = email.value,
                    textLabel = "Email del usuario",
                    onValue = { email.value = it },
                    oneLine = true,
                    shadow = true
                )
                Spacer(modifier = Modifier.height(35.dp))

                CustomBackgroundButton(
                    "Compartir",
                    onClick = { onReady(email.value) },
                    modifier = Modifier
                )
            }


        }
    }
}


@Preview
@Composable
fun ShareDeviceDialogPreview() {
    var email = remember {
        ""
    }
    JETTheme {
        ShareDeviceDialog({}) { }
    }
}