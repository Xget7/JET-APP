package xget.dev.jet.presentation.main.device_config.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.presentation.utils.DevicesTypeObj
import xget.dev.jet.presentation.theme.JetDarkGray
import xget.dev.jet.presentation.theme.JetDarkGray2

@Composable
fun SelectDeviceTypeItem(
    isSelected: Boolean,
    deviceName: String,
    onSelected: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(360.dp)
            .height(60.dp)
            .clickable(onClick = onSelected),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(if (isSelected) Color(0xA1DDEDFE) else Color.Transparent),
        elevation = CardDefaults.elevatedCardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextWithShadow(
                text = deviceName,
                modifier = Modifier,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color(0xC7407BFF) else Color(0xFF73757A),
                fontSize = 20.sp,
                shadow = false
            )

            if (isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.selected_tick),
                    contentDescription = null
                )

            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .border(1.dp, Color(0xC8DEDDDD))
            .padding(end = 16.dp)
    )
    Spacer(modifier = Modifier.height(2.dp))

}