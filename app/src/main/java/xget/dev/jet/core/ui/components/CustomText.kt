package xget.dev.jet.core.ui.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextWithShadow(
    text: String,
    modifier: Modifier,
    fontSize : TextUnit = 16.sp,
            fontWeight : FontWeight,
    color: Color = Color.Black,
    shadow: Boolean = true
) {

    Text(
        text = text,
        style = TextStyle(
            fontSize = fontSize,
            color = color,
            fontWeight = fontWeight,
            shadow = if (shadow) Shadow(
                color = Color(0x1D000000),
                offset = Offset(2.0f, 11.0f),
                blurRadius = 2f
            ) else null
        )
    )
}