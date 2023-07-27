package xget.dev.jet.core.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.presentation.theme.JetBlue
import xget.dev.jet.presentation.theme.JetDarkBlue

@Composable
fun JetTextField(
    text: String,
    textLabel: String,
    onValue: (String) -> Unit,
    trailingIcon: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    oneLine: Boolean = true,
    shadow: Boolean = false,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        value = text,
        onValueChange = onValue,
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFFECEFF7), textColor = Color(0x85000000),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = JetDarkBlue,
            cursorColor = JetBlue
        ),
        textStyle = TextStyle(fontWeight = FontWeight.SemiBold),

        label = {
            Text(textLabel, color = Color(0xD323244E), fontWeight = FontWeight.Bold)
        },
        modifier = modifier
            .width(350.dp)
            .height(60.dp)
            .apply {
                if (shadow) {
                    offset(y = 3.dp)
                    shadow(
                        10.dp,
                        shape = RoundedCornerShape(50),
                        spotColor = Color.Gray,
                        ambientColor = Color.White
                    )
                }
            },

        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = oneLine,

        )

}

@Composable
fun PasswordJetTextField(
    text: String,
    textLabel: String,
    onValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    visibility: MutableState<Boolean> = mutableStateOf(false),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions()
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
        modifier = modifier
            .width(350.dp)
            .height(60.dp),
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        visualTransformation = if (visibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )

}

@Preview
@Composable
fun JetTextFieldPreview() {
    JetTextField(text = "", "", onValue = {})
}