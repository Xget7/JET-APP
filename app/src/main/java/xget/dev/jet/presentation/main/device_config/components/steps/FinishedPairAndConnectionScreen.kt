package xget.dev.jet.presentation.main.device_config.components.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.presentation.utils.Screens

@Composable
fun FinishedPairAndConnectionScreen(gotoHome: () -> Unit) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextWithShadow(
                text = "Dispositivo sincronizado correctamente y listo para su uso.",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color(0xCC000000),
                fontSize = 24.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextWithShadow(
                text = "Ya puedes usarlo cuando quieras.",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF454545),
                fontSize = 16.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(105.dp))

            CustomBackgroundButton(
                text = "Volver",
                containerColor = Color(0xFF407BFF),
                onClick =  gotoHome,
            )

        }
    }
}