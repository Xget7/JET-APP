package xget.dev.jet.presentation.main.history

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopHomeBar
import xget.dev.jet.presentation.main.history.components.SmartDeviceSimpleCard
import xget.dev.jet.presentation.main.home.components.LoadingDevicesListShimmer
import xget.dev.jet.presentation.main.home.components.SmartDeviceItem
import xget.dev.jet.presentation.main.home.device_details.components.ShareDeviceDialog
import xget.dev.jet.presentation.theme.JetGray
import xget.dev.jet.presentation.theme.JetScreensBackgroundColor

@Composable
internal fun DevicesHistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    DevicesHistoryScreen(
        uiState = viewModel.state.collectAsState(),
        lastQuantityOfDevices = viewModel.lastQuantityOfDevices,
    )

}

@Composable
internal fun DevicesHistoryScreen(
    uiState: State<HistoryState>,
    lastQuantityOfDevices: Int //Over-engineering,
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

                            }
                        }
                    }

                }
            }


        }
    }


}

