package xget.dev.jet.presentation.main.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopHomeBar
import xget.dev.jet.core.utils.TestTags.HOME_SCREEN
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.presentation.main.home.components.LoadingDevicesListShimmer
import xget.dev.jet.presentation.main.home.components.SmartDeviceItem
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetScreensBackgroundColor

@Composable
internal fun HomeScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        uiState = vm.state.collectAsState(),
        onSwitchDevice = vm::sendMessageToDevice,
        onAddDevice = {
            navController.navigate(Screens.PairDeviceFirstStep.route)
        },
        onDeviceDetails = {
            navController.navigate(Screens.DeviceDetailScreen.route + "/${it.id}/${it.uid}") {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }, lastQuantityOfDevices = vm.lastQuantityOfDevices
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    uiState: State<HomeUiState>,
    onAddDevice: () -> Unit,
    onSwitchDevice: (SmartDevice) -> Unit,
    onDeviceDetails: (SmartDevice) -> Unit,
    lastQuantityOfDevices: Int
) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(JetScreensBackgroundColor)
            .padding(top = 5.dp, bottom = 55.dp)
            .testTag(HOME_SCREEN),
        scaffoldState = scaffoldState,
        topBar = {
            TopHomeBar(addDevices = true, onAddDevice)
        },
        backgroundColor = JetScreensBackgroundColor,
    ) {
        it

        LaunchedEffect(uiState.value.isError) {
            if (uiState.value.isError != null) {
                Log.d("luanchEffect", "isError ${uiState.value.isError}")
                scaffoldState.snackbarHostState.showSnackbar(
                    uiState.value.isError ?: "Error Inesperado",
                    duration = SnackbarDuration.Short
                )
            }
        }


        Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                //Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp)
                ) {
                    TextWithShadow(
                        text = "Tus Dispositivos",
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        shadow = false,
                        fontSize = 26.sp,
                        color = Color(0xFF4259AD)
                    )

                    //Details
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ellipsis),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp),
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                //Main Content
                if (uiState.value.isLoading && uiState.value.isError == null) {
                    LoadingDevicesListShimmer(lastQuantityOfDevices)
                }

                AnimatedVisibility(
                    visible = !uiState.value.isLoading && uiState.value.isError == null,
                    enter = expandVertically()
                ) {
                    LazyColumn(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (uiState.value.myDevices.value.isEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(200.dp))
                                TextWithShadow(
                                    text = "Aun no tienes dispositivos \n \uD83D\uDE14",
                                    modifier = Modifier,
                                    fontWeight = FontWeight.Bold,
                                    shadow = false,
                                    textAlign = TextAlign.Center,
                                    fontSize = 26.sp,
                                    color = Color(0xFF3A497E)
                                )
                            }
                        }
                        items(uiState.value.myDevices.value.distinct()) { device ->
                            SmartDeviceItem(
                                smartDevice = device,
                                onSwitchToggle = { onSwitchDevice(device) },
                                onDetails = { onDeviceDetails(device) },
                                onLongPress = {}
                            )
                        }
                    }


                }

            }

        }


    }

}


@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPrev() {
    JETTheme {
        HomeScreen(uiState = mutableStateOf(HomeUiState()), {}, {}, {},4)
    }
}