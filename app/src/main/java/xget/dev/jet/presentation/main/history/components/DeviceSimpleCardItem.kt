package xget.dev.jet.presentation.main.history.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.JetSwitchButton
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.utils.TestTags.SMART_DEVICE_HISTORY_ITEM
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.presentation.theme.JetBlue2
import xget.dev.jet.presentation.theme.JetGray2

@Composable
fun SmartDeviceSimpleCard(
    smartDevice: DeviceDto,
    onDetails: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(0.999f)
            .height(125.dp)
            .padding(4.dp)
            .testTag(SMART_DEVICE_HISTORY_ITEM),
        shape = RoundedCornerShape(
            topStart = 15.dp,
            topEnd = 15.dp,
            bottomStart = 19.dp,
            bottomEnd = 19.dp
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize().clickable(onClick = onDetails)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.CenterStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = if (smartDevice.deviceType == "ALARM") R.drawable.alarm else R.drawable.gate_image),
                    contentDescription = "Gate Icon",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(23.dp))
                Column(modifier = Modifier.weight(1f)) {
                    TextWithShadow(
                        text = smartDevice.name,
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        shadow = false
                    )
                }

                IconButton(onClick = onDetails) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Details",
                        tint = JetBlue2,
                        modifier = Modifier.size(50.dp)
                    )
                }


            }
        }

    }


}