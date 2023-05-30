package xget.dev.jet.core.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xget.dev.jet.ui.theme.JetBlue
import xget.dev.jet.ui.theme.JetDarkBlue

@Composable
fun JetTextField(
    text: String,
    textLabel: String,
    onValue: (String) -> Unit,
    trailingIcon:  @Composable() (() -> Unit)? = null
) {

    OutlinedTextField(
        value = text,
        onValueChange = onValue,
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFFECEFF7), textColor = Color(
                0x79000000
            ),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = JetDarkBlue,
            cursorColor = JetBlue
        ),
        label = {
            Text(textLabel, color = Color(0x79000000), fontWeight = FontWeight.SemiBold)
        },
        modifier = Modifier
            .width(350.dp)
            .height(60.dp),
        trailingIcon = trailingIcon

    )

}

@Preview
@Composable
fun JetTextFieldPreview() {
    JetTextField(text = "", "", onValue = {})
}