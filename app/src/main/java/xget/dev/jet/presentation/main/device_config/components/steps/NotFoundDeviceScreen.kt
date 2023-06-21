package xget.dev.jet.presentation.main.device_config.components.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.presentation.theme.JetBlue2

@Composable
fun NotFoundDevice(retry : () -> Unit) {
    //create a close bytton with navigate up

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp).padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TextWithShadow(
            text = "No pudimos encontrar el dispositivo",
            modifier = Modifier,
            fontWeight = FontWeight.Bold,
            color = JetBlue2,
            shadow = false,
            textAlign = TextAlign.Center,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(15.dp))
        TextWithShadow(
            text = "Asegurate de que este en modo emparejamiento y que tu conexion a wifi sea estable.",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Medium,
            color = Color(0xFF454545),
            fontSize = 16.sp,
            shadow = false,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(145.dp))
        CustomBackgroundButton(
            text = "Reintentar ",
            containerColor = Color(0xFF407BFF),
            onClick = retry
        )


    }
}

@Composable
fun ErrorScreen(goBack : () -> Unit) {
    //create a close bytton with navigate up

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp).padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TextWithShadow(
            text = "Estamos teninendo errores de conexion con nuestro servidor.",
            modifier = Modifier,
            fontWeight = FontWeight.Bold,
            color = JetBlue2,
            shadow = false,
            textAlign = TextAlign.Center,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(15.dp))
        TextWithShadow(
            text = "Por favor reintenta mas tarde y revisa tu conectividad.",
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Medium,
            color = Color(0xFF454545),
            fontSize = 16.sp,
            shadow = false,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(145.dp))
        CustomBackgroundButton(
            text = "Volver",
            containerColor = Color(0xFF407BFF),
            onClick = goBack
        )


    }
}