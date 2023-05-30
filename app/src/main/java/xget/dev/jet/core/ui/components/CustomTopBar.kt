package xget.dev.jet.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
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
import xget.dev.jet.ui.theme.JetDarkBlue

@Composable
fun TopCustomBar(
    title: String,
    color: Color = JetDarkBlue
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(painter = painterResource(id = R.drawable.bakcarrow), "Back Arrow")
        Spacer(modifier = Modifier.width(50.dp))
        Text(text = title, fontSize = 26.sp, color = color, fontWeight = FontWeight.Bold)

    }
}

@Preview(showBackground = true)
@Composable
fun TopCustomBar() {
    TopCustomBar(title = "Ingresa a tu cuenta ")
}