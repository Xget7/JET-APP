package xget.dev.jet.core.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetBlue
import xget.dev.jet.presentation.theme.JetGreen

@Composable
fun JetSwitchButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onUpdate: (Boolean) -> Unit
) {
    var horizontalBias by remember { mutableFloatStateOf(-1f) }
    val alignment by animateHorizontalAlignmentAsState(horizontalBias)

    LaunchedEffect(selected){

    }

    Card(
        modifier = modifier
            .width(65.dp)
            .height(35.dp)
            .clickable {
                horizontalBias *= -1
                onUpdate(!selected)
            }, shape = RoundedCornerShape(17.dp), elevation = 0.dp
    ) {
        Box(
            modifier = Modifier.background(
                if (selected) JetGreen else Color(0xFFCCCCCC)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = alignment,
                verticalArrangement = Arrangement.Center
            ) {
                CheckCircle(modifier = Modifier.padding(3.dp))
            }

        }
    }
}

@Composable
fun CheckCircle(
    modifier: Modifier = Modifier
) {

    Card(
        shape = CircleShape, modifier = modifier.size(27.dp), elevation = 0.dp
    ) {
        Box(modifier = Modifier.background(Color.White))
    }

}


@Preview
@Composable
fun JetSwitchButtonPreview() {
    val state = remember {
        mutableStateOf(false)
    }

    JETTheme {
        JetSwitchButton(selected = state.value, onUpdate ={state.value = it})
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(targetBiasValue, label = "")
    return derivedStateOf { BiasAlignment.Horizontal(bias) }
}