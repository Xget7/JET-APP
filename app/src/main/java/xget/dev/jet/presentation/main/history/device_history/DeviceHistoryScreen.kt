package xget.dev.jet.presentation.main.history.device_history

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopHomeBar
import xget.dev.jet.presentation.main.history.components.HistoryDeviceActionCard
import xget.dev.jet.presentation.main.history.components.SmartDeviceSimpleCard
import xget.dev.jet.presentation.main.home.components.LoadingDevicesListShimmer
import xget.dev.jet.presentation.theme.JetGray
import xget.dev.jet.presentation.theme.JetScreensBackgroundColor
import xget.dev.jet.presentation.utils.Screens
import java.util.Locale

@Composable
internal fun DeviceDetailHistoryScreen(
    navController: NavController,
    viewModel: DeviceHistoryViewModel = hiltViewModel()
) {
    DeviceDetailHistoryScreen(
        uiState = viewModel.state.collectAsState(),
        onBack = {
            navController.navigate(Screens.HistoryScreen.route){
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun DeviceDetailHistoryScreen(
    uiState: State<DeviceHistoryState>,
    onBack : () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current


    Scaffold(
        Modifier
            .fillMaxSize()
            .background(JetScreensBackgroundColor)
            .padding(top = 5.dp, bottom = 55.dp),
        scaffoldState = scaffoldState,
        topBar = {
            Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.bakcarrow),
                        "Back Arrow",
                        tint = Color(0xFF407BFF),
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = "Historial",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF435080),
                    )
                )
                Spacer(modifier = Modifier.width(60.dp))
                Icon(
                    painter = painterResource(id = R.drawable.jet_logo),
                    contentDescription = "App Logo",
                    tint = JetGray,

                )
            }
        },
        backgroundColor = JetScreensBackgroundColor,
    ) {


        LaunchedEffect(uiState.value.isError) {
            if (uiState.value.isError != null) {
                scaffoldState.snackbarHostState.showSnackbar(
                    uiState.value.isError ?: context.getString(R.string.error_null_text),
                    duration = SnackbarDuration.Short
                )
            }
        }




        Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
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
                        text = uiState.value.deviceName.capitalize(Locale.getDefault()),
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        shadow = false,
                        fontSize = 32.sp,
                        color = Color(0xFF000D3A)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))

                if (uiState.value.isLoading && uiState.value.isError == null) {
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }

                AnimatedVisibility(
                    visible = !uiState.value.isLoading && uiState.value.isError == null,
                    enter = expandVertically()
                ) {
                    LazyColumn(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        items(uiState.value.history) { action ->
                            HistoryDeviceActionCard(action)
                        }
                    }
                }
            }


        }
    }


}

