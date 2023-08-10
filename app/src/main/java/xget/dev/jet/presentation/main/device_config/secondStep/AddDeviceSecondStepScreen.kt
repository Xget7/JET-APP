package xget.dev.jet.presentation.main.device_config.secondStep

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import xget.dev.jet.MainActivity
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.PasswordJetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.core.utils.TestTags
import xget.dev.jet.core.utils.TestTags.GO_TO_ADD_DEVICE_STEP_3_BTN
import xget.dev.jet.core.utils.checkLocationSetting
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.theme.JETTheme

@Composable
internal fun AddDeviceSecondStep(
    navController: NavController,
    viewModel: AddDeviceSecondStepViewModel = hiltViewModel()
) {
    AddDeviceSecondStep(
        uiState = viewModel.state.collectAsState(),
        wifiSsid = viewModel.wifiSsid,
        wifiPassword = viewModel.wifiPassword,
        updateWifiPassword = viewModel::changeWifiPassword,
        updateWifiSsid = viewModel::changeWifiSSid,
        onNext = {
            viewModel.saveWifiCredentials()
            navController.navigate(Screens.PairDeviceThirdStep.route)
        },
        getWifiSsid = viewModel::getWifiSsid,
        onBack = {
            navController.navigateUp()
        }
    )


}

@Composable
internal fun AddDeviceSecondStep(
    uiState: State<AddDeviceSecondStepUiState>,
    wifiSsid: String,
    wifiPassword: String,
    updateWifiSsid: (String) -> Unit,
    updateWifiPassword: (String) -> Unit,
    onNext: () -> Unit,
    getWifiSsid: () -> Unit,
    onBack: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.BLUETOOTH_SCAN] == true &&
            permissions[Manifest.permission.BLUETOOTH_CONNECT] == true) {
            // Bluetooth permissions are granted, proceed with onNext
            onNext()
        } else {
            // Show an error message or inform the user that Bluetooth permissions are required
        }
    }


    val wifiPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        for ((permission, isGranted) in permissions) {
            if (isGranted) {
                // Permission is granted
                // Do something

                checkLocationSetting(context as MainActivity,
                    onDisabled = {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                "Nececitamos tu localizacion para obtener el nombre de tu red.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onEnabled = {
                        getWifiSsid()
                    }
                )
            } else {
                // Permission is denied
                // Do something else, like showing an error message
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Nececitamos tus permisos de localizacion para obtener el nombre de tu red.",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }


    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 12.dp),
        topBar = {
            TopCustomBar(title = "", onClick = onBack)
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomBackgroundButton(
                    text = "Siguiente",
                    containerColor = Color(0xFF407BFF),
                    onClick = {
                        permissionLauncher?.launch(
                            arrayOf(
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT
                            )
                        )
                    },
                    modifier = Modifier.testTag(GO_TO_ADD_DEVICE_STEP_3_BTN)
                )

            }
        },
        backgroundColor = Color.White
    ) { paddingVals ->

        LaunchedEffect(true) {
            wifiPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingVals)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(30.dp))

            TextWithShadow(
                text = "Selecciona una red WiFi de 2.4GHz",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = Color(0xCC000000),
                fontSize = 24.sp,
                shadow = false,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))


            Image(
                painter = painterResource(id = R.drawable.wifi_grpah),
                contentDescription = null,
                modifier = Modifier
                    .width(360.dp)
                    .height(168.dp)
            )

            Spacer(modifier = Modifier.height(60.dp))

            JetTextField(
                text = wifiSsid,
                textLabel = "Nombre de la red",
                onValue = updateWifiSsid,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.wifi_icon),
                        contentDescription = null
                    )
                },
                modifier = Modifier.testTag(TestTags.USER_WIFI_SSID_TEXT_FIELD)
            )
            Spacer(modifier = Modifier.height(20.dp))

            PasswordJetTextField(
                text = wifiPassword,
                textLabel = "Contraseña",
                onValue = updateWifiPassword,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.password_icon),
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .testTag(TestTags.USER_WIFI_PASSWORD_TEXT_FIELD)
            )


        }


    }


}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun SecondStepPrev() {
    JETTheme {
        val uiState = mutableStateOf(AddDeviceSecondStepUiState(wifiSsid = ""))
        AddDeviceSecondStep(
            uiState = uiState,
            wifiSsid = "Movistar-4600E",
            wifiPassword = "",
            updateWifiSsid = {},
            updateWifiPassword = {},
            {},
            {},
            {}
        )
    }
}


private fun Context.hasBluetoothPermissions(): Boolean {
    return (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)
}