package xget.dev.jet.presentation.main.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.ui.components.TopHomeBar
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetScreensBackgroundColor

@Composable
internal fun HomeScreen(
    navController: NavController,
    vm: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(true){
        navController.navigate(Screens.PairDeviceThirdStep.route)
    }
//    HomeScreen(uiState = vm.state.collectAsState()) {
//        navController.navigate(Screens.PairDeviceFirstStep.route)
//    }

}


@Composable
internal fun HomeScreen(
    uiState: State<HomeUiState>,
    onAddDevice: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .background(JetScreensBackgroundColor)
            .padding(top = 5.dp),
        scaffoldState = scaffoldState,
        topBar = {
            TopHomeBar(addDevices = true,onAddDevice)
        },
        backgroundColor = JetScreensBackgroundColor
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
                        text = "Tus Dispositivos",
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        shadow = false,
                        fontSize = 26.sp,
                        color = Color(0xFF435080)
                    )

                    //Details
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ellipsis),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                //Main Content


            }

        }


    }

}


@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPrev() {
    JETTheme {
        HomeScreen(uiState = mutableStateOf(HomeUiState())){}
    }
}