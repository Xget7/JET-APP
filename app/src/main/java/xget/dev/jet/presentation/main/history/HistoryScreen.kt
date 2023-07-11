package xget.dev.jet.presentation.main.history

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopHomeBar
import xget.dev.jet.presentation.main.history.components.SmartDeviceSimpleCard
import xget.dev.jet.presentation.main.home.components.LoadingDevicesListShimmer
import xget.dev.jet.presentation.theme.JetScreensBackgroundColor
import xget.dev.jet.presentation.utils.Screens

@Composable
internal fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    HistoryScreen(
        uiState = viewModel.state.collectAsState(),
        lastQuantityOfDevices = viewModel.lastQuantityOfDevices,
        onDeviceDetail = {
            navController.navigate(Screens.DeviceHistoryScreen.route + "/${it}") {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    )

}

@Composable
internal fun HistoryScreen(
    uiState: State<HistoryState>,
    lastQuantityOfDevices: Int, //Over-engineering.
    onDeviceDetail: (String) -> Unit
) {



    val scaffoldState = rememberScaffoldState()



    Scaffold(
        Modifier
            .fillMaxSize()
            .background(JetScreensBackgroundColor)
            .padding(top = 5.dp, bottom = 55.dp),
        scaffoldState = scaffoldState,
        topBar = {
            TopHomeBar(addDevices = false, onClick = { /*TODO*/ })
        },
        backgroundColor = JetScreensBackgroundColor,
    ) {
        it


        LaunchedEffect(uiState.value.isError) {
            if (uiState.value.isError != null) {
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
                        text = "Historial",
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        shadow = false,
                        fontSize = 26.sp,
                        color = Color(0xFF435080)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                if (uiState.value.isLoading && uiState.value.isError == null) {
                    LoadingDevicesListShimmer(lastQuantityOfDevices)
                } else {
                    LazyColumn(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.value.devices) { device ->
                            SmartDeviceSimpleCard(device) {
                                onDeviceDetail(device.id)
                            }
                        }
                    }

                }
            }


        }
    }


}

