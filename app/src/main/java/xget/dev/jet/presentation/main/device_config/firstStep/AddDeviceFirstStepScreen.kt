package xget.dev.jet.presentation.main.device_config.firstStep

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xget.dev.jet.MainActivity
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.JetTextField
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopCustomBar
import xget.dev.jet.core.utils.TestTags.DEVICE_NAME_TEXT_FIELD
import xget.dev.jet.core.utils.TestTags.GO_TO_ADD_DEVICE_STEP_2
import xget.dev.jet.core.utils.checkLocationSetting
import xget.dev.jet.presentation.main.device_config.components.SelectDeviceTypeItem
import xget.dev.jet.presentation.main.device_config.components.TurnOnBluetooth
import xget.dev.jet.presentation.utils.DevicesTypeObj.listOfDevices
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetDarkGray2

@SuppressLint("UnrememberedMutableState")
@Composable
internal fun AddDeviceFirstStep(
    navController: NavController,
    viewModel: AddDeviceFirstStepViewModel = hiltViewModel()
) {


    AddDeviceFirstStep(
        uiState = viewModel.state.collectAsState(),
        viewModel::changeSelectedDevice,
        onNext = {
            navController.navigate(Screens.PairDeviceSecondStep.route)
        },
        onBack = navController::navigateUp,
        tryEnableLocation = viewModel::tryEnableLocation,
        deviceName = viewModel.deviceName.value,
        updateDeviceName = viewModel::updateDeviceName
    )


}


@Composable
internal fun AddDeviceFirstStep(
    uiState: State<AddDeviceFirstStepUiState>,
    selectedItem: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    tryEnableLocation: () -> Unit,
    deviceName: String,
    updateDeviceName: (String) -> Unit
) {

    //permissions and launchers

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()


    // TODO maybe need to be before OnCreate because lifcicleOwners error
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->


        val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
        } else true

        if (canEnableBluetooth && !uiState.value.bluetoothOn) {
            enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }

    val launchGps =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Your logic

            }
        }

    LaunchedEffect(uiState.value.intentSenderForResult) {
        if (uiState.value.intentSenderForResult != null) {
            launchGps.launch(uiState.value.intentSenderForResult)
        }
    }


    val wifiPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        for ((permission, isGranted) in permissions) {
            if (isGranted) {
                // Permission is granted
                // Do something
                checkLocationSetting(
                    context as MainActivity,
                    onDisabled = tryEnableLocation,
                    onEnabled = tryEnableLocation
                )
            } else {
                // Permission is denied
                // Do something else, like showing an error message
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Nececitamos permisos para continuar.",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }


    LaunchedEffect(true) {
        wifiPermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 50.dp),
        topBar = {
            TopCustomBar(title = "Añadir Dispositivos", onClick = onBack)
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
                    text = "Continuar",
                    containerColor = Color(0xFF407BFF),
                    onClick = {
                        if (uiState.value.gpsOn) {
                            onNext()
                        } else {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Nececitamos tu localizacion para continuar.",
                                    duration = SnackbarDuration.Long
                                )
                                delay(1000)
                                tryEnableLocation()
                            }
                        }
                    }, modifier = Modifier.testTag(GO_TO_ADD_DEVICE_STEP_2)
                )

            }
            Spacer(modifier = Modifier.height(20.dp))
        },
        backgroundColor = Color.White
    ) { paddingVals ->
        Column(
            modifier = Modifier
                .padding(paddingVals)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))


            AnimatedVisibility(
                visible = !uiState.value.bluetoothOn,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                TurnOnBluetooth {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,

                            )
                    )
                }
            }




            Spacer(modifier = Modifier.height(20.dp))

            TextWithShadow(
                text = "Selecciona tu dispositivo:",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = JetDarkGray2,
                fontSize = 24.sp,
                shadow = false
            )
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.height(270.dp)
            ) {
                items(listOfDevices) { name ->
                    SelectDeviceTypeItem(
                        isSelected = uiState.value.selectedItem == name,
                        deviceName = name
                    ) {
                        selectedItem(name)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            TextWithShadow(
                text = "Asignale un nombre",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = JetDarkGray2,
                fontSize = 23.sp,
                shadow = false
            )
            Spacer(modifier = Modifier.height(15.dp))

            JetTextField(
                text = deviceName,
                textLabel = "Nombre de tu dispositivo",
                onValue = updateDeviceName,
                modifier = Modifier.testTag(DEVICE_NAME_TEXT_FIELD)
            )


        }


    }


}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun FirstStepPrev() {
    JETTheme {
        val selected = mutableStateOf("Alarma")
        val uiState = mutableStateOf(
            AddDeviceFirstStepUiState(
                selectedItem = selected.value,
                bluetoothOn = false
            )
        )
        AddDeviceFirstStep(uiState = uiState, {}, {}, {}, {}, "", {})
    }
}




