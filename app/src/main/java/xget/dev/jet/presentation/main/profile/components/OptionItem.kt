package xget.dev.jet.presentation.main.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.presentation.theme.JETTheme

@Composable
fun OptionItem(
    title: String,
    description: String,
    onClick: () -> Unit
) {

    val gray = Color(0xFF000000).copy(alpha = 0.45F)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(51.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF435080)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = gray
            )
        }
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                tint = gray,
                modifier = Modifier.size(35.dp)
            )
        }
    }


}

@Preview
@Composable
fun OptionItemPreview() {
    JETTheme {
        OptionItem("Configracion del perfil", "Modifica y actuazlia tu perfil") {

        }
    }
}