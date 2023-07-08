package xget.dev.jet.presentation.main.home.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.JetSwitchButton
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetGray2
import xget.dev.jet.presentation.utils.DevicesTypeObj.getDeviceTypes

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun SmartDeviceItem(
    smartDevice: SmartDevice,
    onSwitchToggle: () -> Unit,
    onDetails: () -> Unit,
    onLongPress: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    //TODO(custom picture for each device

    Card(
        modifier = Modifier
            .fillMaxWidth(0.99f)
            .height(125.dp)
            .padding(3.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            )
            .combinedClickable(
                onClick = {},
                onLongClick = onLongPress,
            ),
        shape = RoundedCornerShape(
            topStart = 10.dp,
            topEnd = 10.dp,
            bottomStart = 15.dp,
            bottomEnd = 15.dp
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onDetails
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .padding(6.dp)
                    .size(10.dp)
                    .align(Alignment.TopStart),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = if (smartDevice.online) Color(
                        0xff4FBE79
                    ) else Color(0xFFE3775F)
                )
            ) {}
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.CenterStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = if (smartDevice.type == "Alarma") R.drawable.alarm else R.drawable.gate_image),
                    contentDescription = "Gate Icon",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(23.dp))
                Column(modifier = Modifier.weight(1f)) {
                    TextWithShadow(
                        text = smartDevice.name.capitalize(),
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        shadow = false
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = if (smartDevice.online) "Online" else "Offline",
                        fontSize = 14.sp,
                        color = JetGray2
                    )
                }

                JetSwitchButton(
                    selected = smartDevice.stateValue.intValue == 1,
                    onUpdate = { onSwitchToggle() },
                    state = smartDevice.stateValue.intValue

                )


            }
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun SmartDevicePreview() {
    JETTheme {
        Box(modifier = Modifier.size(400.dp)) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                SmartDeviceItem(SmartDevice(
                    name = "Portón de casa",
                    online = true,
                    stateValue = mutableIntStateOf(0)
                ),
                    {}, {}, {})

            }

        }
    }
}