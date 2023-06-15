package xget.dev.jet

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import xget.dev.jet.presentation.main.MainScreens
import xget.dev.jet.presentation.main.device_config.device_search.DeviceSearchScreen
import xget.dev.jet.presentation.main.device_config.firstStep.AddDeviceFirstStep
import xget.dev.jet.presentation.main.device_config.secondStep.AddDeviceSecondStep
import xget.dev.jet.presentation.main.home.HomeScreen
import xget.dev.jet.presentation.main.home.HomeUiState
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.utils.Screens.HistoryScreen
import xget.dev.jet.presentation.utils.Screens.HomeNavGraph
import xget.dev.jet.presentation.utils.Screens.HomeScreen
import xget.dev.jet.presentation.utils.Screens.ProfileScreen
import xget.dev.jet.ui.theme.JETTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {
            JETTheme {
                val navController = rememberNavController()
                MainScreens(navController = navController)
            }

        }
    }

}

@Composable
fun HomeBottomNav(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = HomeNavGraph.route,
        startDestination = HomeScreen.route
    ) {
        composable(route = HomeScreen.route){
            HomeScreen(navController)
        }
        composable(route = ProfileScreen.route){

        }
        composable(route = HistoryScreen.route){

        }
        bluetoothNavGraph(navController)
    }
}

fun NavGraphBuilder.bluetoothNavGraph(navController: NavHostController) {
    navigation(
        route = Screens.BluetoothNavGraph.route,
        startDestination = Screens.PairDeviceFirstStep.route
    ) {

        composable(route = Screens.PairDeviceFirstStep.route) {
            AddDeviceFirstStep(navController)
        }
        composable(route = Screens.PairDeviceSecondStep.route) {
            AddDeviceSecondStep(navController)
        }
        composable(route = Screens.PairDeviceThirdStep.route) {
            DeviceSearchScreen(navController)
        }
    }
}


