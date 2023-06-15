package xget.dev.jet.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue

@Composable
fun CustomBackgroundButton(
    text: String = "",
    textColor: Color = Color.White,
    containerColor: Color = JetBlue,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(30),
        modifier = modifier
            .width(340.dp)
            .height(55.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Medium)

        }

    }
}

@Preview
@Composable
fun CustomBackButtonprev() {
    JETTheme {
        CustomBackgroundButton(onClick = {})
    }
}