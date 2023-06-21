package xget.dev.jet.presentation.main.device_config.device_search

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import xget.dev.jet.presentation.main.device_config.components.steps.DeviceFoundDevicesStep
import xget.dev.jet.presentation.main.device_config.components.steps.NotFoundDevice
import xget.dev.jet.presentation.main.device_config.components.SearchDeviceStepper
import xget.dev.jet.presentation.main.device_config.components.steps.ErrorScreen
import xget.dev.jet.presentation.main.device_config.components.steps.FinishedPairAndConnectionScreen
import xget.dev.jet.presentation.main.device_config.components.steps.SearchingDevicesStep
import xget.dev.jet.presentation.main.device_config.components.steps.UploadDeviceToCloudStep
import xget.dev.jet.presentation.utils.Screens

@Composable
internal fun DeviceSearchScreen(
    navController: NavController,
    viewModel: DeviceSearchViewModel = hiltViewModel()
) {


    DeviceSearchScreen(
        uiState = viewModel.state.collectAsState(),
        viewModel::retry
    ) {
        navController.navigate(Screens.HomeScreen.route)
    }

}


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun DeviceSearchScreen(
    uiState: State<DeviceSearchUiState>,
    retry: () -> Unit,
    goToHome: () -> Unit,
) {

    val scaffoldState = rememberScaffoldState()
    val pageState = rememberPagerState()


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




    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 1.dp),
        bottomBar = {

        },
        backgroundColor = Color.White
    ) { paddingValues ->
        paddingValues


        LaunchedEffect(uiState.value.errorMessage) {
            if (!uiState.value.errorMessage.isNullOrEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(
                    uiState.value.errorMessage!!,
                    duration = SnackbarDuration.Long
                )
            }

        }
        LaunchedEffect(uiState.value.syncingWithCloud) {
            if (uiState.value.syncingWithCloud && !uiState.value.pairingDevice) {
                pageState.animateScrollToPage((2) % 3)
            }
        }
        LaunchedEffect(uiState.value.finished) {
            if (uiState.value.finished == true) {
                Log.d("animatetoScrollPage", "3")
                pageState.animateScrollToPage((3) % 3)
            }
        }


        if (uiState.value.cantFindDevice) {
            NotFoundDevice(retry)
        } else if (!uiState.value.errorMessage.isNullOrEmpty()) {
            ErrorScreen(goToHome)
        }else{
            HorizontalPager(
                contentPadding = PaddingValues(12.dp),
                itemSpacing = 26.dp,
                state = pageState,
                count = 4,
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
                    }
                    3 -> {
                        FinishedPairAndConnectionScreen(goToHome)
                    }
                }


            }
        }

        if (!uiState.value.cantFindDevice) {
            if (uiState.value.errorMessage.isNullOrEmpty()){
                Box(modifier = Modifier.fillMaxHeight()) {
                    Row(
                        Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchDeviceStepper(pageState.currentPage + 1)
                    }
                }
            }


        }

    }

}






