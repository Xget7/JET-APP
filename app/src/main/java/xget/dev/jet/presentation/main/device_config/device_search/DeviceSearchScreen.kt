package xget.dev.jet.presentation.main.device_config.device_search

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar

@Composable
internal fun DeviceSearchScreen(
    navController: NavController,
    viewModel: DeviceSearchViewModel = hiltViewModel()
) {
    DeviceSearchScreen(uiState = viewModel.state.collectAsState(), viewModel.content.intValue , viewModel::pairDevice)

}


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DeviceSearchScreen(
    uiState : State<DeviceSearchUiState>,
    counter : Int,
    pairDevice: () -> Unit

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
        val canEnableBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms[Manifest.permission.BLUETOOTH_CONNECT] == true
        } else true

        if(canEnableBluetooth && !uiState.value.bluetoothOn) {
            enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }


    SideEffect {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT))
        }
    }




    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 12.dp),
        topBar = {
            TopCustomBar(title = "", showBack = false){}
        },
        bottomBar = {
        },
        backgroundColor = Color.White
    ) { paddingValues ->
        paddingValues

//        LaunchedEffect(uiState){
//            Log.d("ScannedDevices", "${uiState.value.scannedDevices}")
//            if (uiState.value.scannedDevices.any { it.name.contains("JET") }){
//                Log.d("ScannedDevice", "True , pair true")
//                pairDevice(uiState.value.scannedDevices.first { it.name.contains("JET") })
//            }
//        }

        Column {
            Spacer(modifier = Modifier.height(120.dp))

            Text(text = "Searching Device State" + uiState.value.searchingDevice)
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Pairing Device State" + uiState.value.pairingDevice)
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Syncing with cloud? Device State" + uiState.value.syncingWithCloud)
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Error Message State" + uiState.value.errorMessage)
            Text(text = "Timer $counter")

            LazyColumn(){
                items(uiState.value.scannedDevices){
                    LaunchedEffect(it ){
                        if (it.name.startsWith("JET-IOT")){

                        }
                    }
                    Text(text = "ADdress" + it.address)
                    Text(text = "Name" + it.name)

                }
            }
        }



    }
//
//    HorizontalPager(
//        contentPadding = PaddingValues(32.dp),
//        itemSpacing = 16.dp,
//        count = 2,
//    ) {
//
//    }



}

@Composable
fun SearchingDevicesStep() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url("https://assets8.lottiefiles.com/packages/lf20_LKXG6QRgtE.json"))

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

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

            LottieAnimation(composition = composition)

        }


    }
}

@Composable
fun UploadDeviceToCloudStep() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url("https://assets10.lottiefiles.com/packages/lf20_rd1Sr6pYMx.json"))

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.height(15.dp))
            LottieAnimation(composition = composition)
        }


    }
}