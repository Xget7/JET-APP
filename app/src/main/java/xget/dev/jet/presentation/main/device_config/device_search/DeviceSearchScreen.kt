package xget.dev.jet.presentation.main.device_config.device_search

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.presentation.main.device_config.components.SearchDeviceStepper

@Composable
internal fun DeviceSearchScreen(
    navController: NavController,
    viewModel: DeviceSearchViewModel = hiltViewModel()
) {
    DeviceSearchScreen(
        uiState = viewModel.state.collectAsState(),
        viewModel.content.intValue,
    )

}


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DeviceSearchScreen(
    uiState: State<DeviceSearchUiState>,
    counter: Int,

    ) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    //Manage Bluetooth Permissions


    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Not needed */ }

    val requestMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms[Manifest.permission.BLUETOOTH_CONNECT] == true
        } else true

        if (canEnableBluetooth && !uiState.value.bluetoothOn) {
            enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }


    LaunchedEffect(true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                )
            )
        } else {
            requestMultiplePermissions.launch(


                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                )
            )
        }
    }

    val pageState = rememberPagerState()



    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 1.dp),
        topBar = {
        },
        bottomBar = {
            Row(
                Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchDeviceStepper(pageState.currentPage + 1)

            }
        },
        backgroundColor = Color.White
    ) { paddingValues ->
        paddingValues
        var pageSize by remember { mutableStateOf(IntSize.Zero) }
        val lastIndex by remember(pageState.currentPage) {
            derivedStateOf {pageState.currentPage == 3 - 1 }
        }

        LaunchedEffect(uiState.value.syncingWithCloud) {
           if (uiState.value.syncingWithCloud && !uiState.value.pairingDevice) {
                Log.d("LaunchedEffect state cloud ", uiState.value.syncingWithCloud.toString())
                pageState.animateScrollToPage((pageState.currentPage + 1) % 3)
            }
        }



        HorizontalPager(
            contentPadding = PaddingValues(32.dp),
            itemSpacing = 26.dp,
            state = pageState,
            count = 3,
            userScrollEnabled = false,
            ) { page ->
            when (page) {
                0 -> {
                    SearchingDevicesStep(uiState, pageState)
                }

                1 -> {
                    DeviceFoundDevicesStep()
                }

                2 -> {
                    UploadDeviceToCloudStep()
                    //mange errors and change screens
                }
            }
        }


    }

}

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


    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(0.dp))

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

            Spacer(modifier = Modifier.height(15.dp))

            if (uiState.value.pairingDevice) {
                LottieAnimation(composition = compositionResult.value, progressResult)
                LaunchedEffect(progressResult) {
                    pageState.animateScrollToPage(1)
                }
            } else {
                LottieAnimation(composition = searching, progress = progress)
            }


        }
    }
}


@Composable
fun DeviceFoundDevicesStep() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url("https://lottie.host/02ce23fa-95b8-409f-b61c-f0270427eceb/DTQazBhB0S.json"))

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = true
    )

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            TextWithShadow(
                text = "Añadiendo y Configurando Dispositivo",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color(0xCC000000),
                fontSize = 24.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(25.dp))
            TextWithShadow(
                text = "Asegurate que la señal wifi sea buena.",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF454545),
                fontSize = 16.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(155.dp))

            LottieAnimation(composition = composition, progress)

        }


    }
}

@Composable
fun UploadDeviceToCloudStep() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_rd1Sr6pYMx.json"))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        speed = 1f,
        restartOnPlay = true
    )
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(0.dp))

            TextWithShadow(
                text = "Configurando dispositivo en la Nube",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color(0xCC000000),
                fontSize = 24.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextWithShadow(
                text = "Vincularemos este dispositivo a tu usuario.",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium,
                color = Color(0xFF454545),
                fontSize = 16.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(155.dp))
            LottieAnimation(composition = composition, progress)
        }


    }
}