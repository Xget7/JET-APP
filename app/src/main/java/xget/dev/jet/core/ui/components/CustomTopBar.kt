package xget.dev.jet.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import xget.dev.jet.presentation.theme.JetBlue
import xget.dev.jet.presentation.theme.JetDarkBlue
import xget.dev.jet.presentation.theme.JetGray

@Composable
fun TopCustomBar(
    title: String,
    color: Color = JetDarkBlue,
    showBack: Boolean = true,
    onClick: () -> Unit,
) {
    Box() {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showBack) {
                    IconButton(onClick = onClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.bakcarrow),
                            "Back Arrow",
                            tint = Color(0xFF949494)
                        )
                    }
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if(showBack) Arrangement.Start else Arrangement.Center
            ) {

                if (showBack){
                    Spacer(modifier = Modifier.width(34.dp))
                }
                Text(text = title, fontSize = 27.sp, color = color, fontWeight = FontWeight.Bold)

            }
        }

    }

}


@Composable
fun TopHomeBar(addDevices: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(26.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.jet_logo),
            contentDescription = "App Logo",
            tint = JetGray
        )

        if (addDevices){
            IconButton(onClick = onClick) {
                Icon(
                    painter = painterResource(id = R.drawable.add_device_icon),
                    contentDescription = "Add device Icon",
                    tint = Color(0xFF0F7AFF),
                    modifier = Modifier.size(40.dp)
                )

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TopHomeBarPreview() {
    TopHomeBar(addDevices = true) {

    }
}


@Preview(showBackground = true)
@Composable
fun TopCustomBar() {
    TopCustomBar(
        title = "Ingresa a tu cuenta ",
        showBack = false,
        color = JetDarkBlue,
        onClick = {})
}