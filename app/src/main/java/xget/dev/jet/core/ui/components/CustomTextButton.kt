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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue

@Composable
fun CustomTextButton(
    text: String = "",
    textColor: Color = Color.White,
    onClick: () -> Unit,
    shadow : Boolean = true,
    fontSize : TextUnit = 24.sp
) {
    TextButton(onClick = onClick, modifier = Modifier
        .width(340.dp)
        .height(60.dp)) {
        TextWithShadow(
            text = text,
            color = textColor,
            fontSize = fontSize,
            modifier = Modifier,
            fontWeight = FontWeight.SemiBold,
            shadow = shadow
        )
    }
}

@Preview
@Composable
fun CustomTextButtonPrev() {
    JETTheme {
        CustomTextButton(onClick = {})
    }
}