package xget.dev.jet.presentation.main.home.components

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.JetSwitchButton
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetGray2

@Composable
fun ShimmerDeviceItem(
    colors: List<Color>,
    xShimmer: Float,
    yShimmer: Float,
    gradientWidth: Float,
) {
    val brush = linearGradient(
        colors,
        start = Offset(xShimmer - gradientWidth, yShimmer - gradientWidth),
        end = Offset(xShimmer, yShimmer)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(0.99f)
            .height(125.dp)
            .padding(3.dp),
        shape = RoundedCornerShape(
            topStart = 10.dp,
            topEnd = 10.dp,
            bottomStart = 15.dp,
            bottomEnd = 15.dp
        ),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .padding(6.dp)
                    .size(13.dp)
                    .align(Alignment.TopStart),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = White)
            ) {}
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .align(Alignment.CenterStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(60.dp)
                            .background(brush = brush)
                    )
                }

                Spacer(modifier = Modifier.width(23.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.height(14.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .width(160.dp)
                                .background(brush = brush)
                        )
                    }
                    Spacer(modifier = Modifier.height(23.dp))
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.height(14.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .width(80.dp)
                                .background(brush = brush)
                        )
                    }

                }


                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.height(40.dp),
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(65.dp)
                            .background(brush = brush)
                    )
                }


            }
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PrevShimmer() {

    JETTheme {


        val colors = listOf(
            Color.LightGray.copy(alpha = .9f),
            Color.LightGray.copy(alpha = .3f),
            Color.LightGray.copy(alpha = .9f),
        )

        Column {
            ShimmerDeviceItem(
                colors = colors,
                xShimmer = 1f,
                yShimmer = 0.5f,
                gradientWidth = 1f,
            )


            SmartDeviceItem(smartDevice = SmartDevice("3232", name = "Porton Juan", online = true),
                {},
                {}) {
            }
        }
    }
}
