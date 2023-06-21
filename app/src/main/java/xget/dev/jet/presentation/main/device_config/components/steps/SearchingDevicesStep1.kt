package xget.dev.jet.presentation.main.device_config.components.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.presentation.main.device_config.device_search.DeviceSearchUiState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchingDevicesStep(
    uiState: State<DeviceSearchUiState>,
    pageState: PagerState
) {


    val compositionResult: LottieCompositionResult =
        rememberLottieComposition(LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_E1OBOP.json"))


    val searching by rememberLottieComposition(
        LottieCompositionSpec.Url("https://assets8.lottiefiles.com/packages/lf20_LKXG6QRgtE.json")
    )

    val progressResult by animateLottieCompositionAsState(
        searching,
        iterations = 1,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = true
    )

    val progress by animateLottieCompositionAsState(
        searching,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = true
    )


    Box(modifier = Modifier.background(Color.White).padding(top = 46.dp)) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextWithShadow(
                text = "Buscando dispositivos",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color(0xCC000000),
                fontSize = 24.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextWithShadow(
                text = "Comproba que el dispositivo este cerca de tu celular.",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF454545),
                fontSize = 16.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(0.dp))

            if (uiState.value.pairingDevice) {
                LottieAnimation(composition = compositionResult.value, progressResult)
                LaunchedEffect(progressResult) {
                    pageState.animateScrollToPage(1)
                }
            } else {
                LottieAnimation(composition = searching, progress = progress)
            }



        }

        TextWithShadow(
            text = uiState.value.timeUntilStop,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            fontWeight = FontWeight.Medium,
            color = Color(0xFF111111),
            fontSize = 36.sp,
            shadow = false,
            textAlign = TextAlign.Center
        )
    }
}