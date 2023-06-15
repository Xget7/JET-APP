package xget.dev.jet.presentation.main.device_config.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue
import xget.dev.jet.ui.theme.JetDarkBlue
import xget.dev.jet.ui.theme.JetDarkGray
import xget.dev.jet.ui.theme.JetDarkGray2
import xget.dev.jet.ui.theme.JetGray

@Composable
fun TurnOnBluetooth(
    onClick: () -> Unit
) {

    Box(modifier = Modifier.padding(14.dp)) {

        Column {

            Text(buildAnnotatedString {

                append("Buscando dispositivos cercanos. Asegurate que la luz")

                withStyle(SpanStyle(color = JetDarkBlue)) {
                    append(" azul ")
                }

                append("titile.")

            }, fontSize = 15.sp, color = JetDarkGray2, fontWeight = FontWeight.Medium)


            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier
                    .width(350.dp)
                    .height(60.dp).clickable(onClick = onClick),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F7FB))
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextWithShadow(
                        text = "Encender Bluetooth",
                        modifier = Modifier,
                        fontWeight = FontWeight.SemiBold,
                        color = JetDarkGray2,
                        fontSize = 18.sp,
                        shadow = false
                    )

                    Image(
                        painter = painterResource(id = R.drawable.bluetooth_off),
                        contentDescription = null,
                    )


                }

            }
        }


    }


}

@Preview
@Composable
fun turnOnbluetoothPrev() {
    JETTheme {
        TurnOnBluetooth({})
    }
}