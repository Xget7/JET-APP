package xget.dev.jet.presentation.main.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.core.utils.convertTimestamp
import xget.dev.jet.data.remote.devices.rest.dto.history.DeviceActionReq
import xget.dev.jet.domain.model.device.SmartDeviceAction

@Composable
fun HistoryDeviceActionCard(
    action: SmartDeviceAction
) {

    Card(
        modifier = Modifier
            .width(352.dp)
            .height(80.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(size = 15.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCFCFC)),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Sharp.AccountCircle,
                    contentDescription = "User",
                    tint = Color(0xFF6E89EA),
                    modifier = Modifier.size(50.dp)
                )

                Text(
                    text = action.userName,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xBA000000),
                        textAlign = TextAlign.Center,
                    )
                )

                Text(
                    text = if (action.state) "ENCENDIO" else "APAGO",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(600),
                        color = if (action.state) Color(0xFF11CE00) else Color(0xFFE13D09),
                    ),
                    modifier = Modifier.width(80.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd).padding(end = 14.dp).padding(bottom = 4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = convertTimestamp(action.time),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0x66000000),
                        textAlign = TextAlign.Center,
                    )
                )
            }
        }

    }


}