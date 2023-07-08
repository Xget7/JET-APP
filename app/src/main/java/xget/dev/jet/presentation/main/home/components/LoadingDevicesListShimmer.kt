package xget.dev.jet.presentation.main.home.components

import android.content.SharedPreferences
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.util.SharedPreferencesUtils
import xget.dev.jet.presentation.theme.JETTheme

@Composable
fun LoadingDevicesListShimmer(devices:Int){

    val preferences =
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val cardWidthPx = with(LocalDensity.current) { (maxWidth - (4.dp*2)).toPx() }
        val cardHeightPx = with(LocalDensity.current) { (60.dp - 4.dp).toPx() }
        val gradientWidth: Float = (0.2f * cardHeightPx)

        val infiniteTransition = rememberInfiniteTransition()
        val xCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardWidthPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
        val yCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardHeightPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )

        val colors = listOf(
            Color.LightGray.copy(alpha = .7f),
            Color.LightGray.copy(alpha = .3f),
            Color.LightGray.copy(alpha = .7f),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(devices){
                ShimmerDeviceItem(
                    colors = colors,
                    xShimmer = xCardShimmer.value,
                    yShimmer = yCardShimmer.value,
                    gradientWidth = gradientWidth,
                )
            }
        }
    }
}

@Preview
@Composable
fun LoadingDeviceShimmerPrev() {
    JETTheme {
        LoadingDevicesListShimmer(4)
    }
}