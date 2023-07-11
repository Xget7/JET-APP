package xget.dev.jet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import xget.dev.jet.presentation.main.history.HistoryScreen
import xget.dev.jet.presentation.main.history.device_history.DeviceDetailHistoryScreen
import xget.dev.jet.presentation.main.home.HomeScreen
import xget.dev.jet.presentation.main.home.device_details.DeviceDetailScreen
import xget.dev.jet.presentation.main.profile.ProfileScreen
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.utils.Screens.HistoryScreen
import xget.dev.jet.presentation.utils.Screens.HomeNavGraph
import xget.dev.jet.presentation.utils.Screens.HomeScreen
import xget.dev.jet.presentation.utils.Screens.ProfileScreen
import xget.dev.jet.presentation.theme.JETTheme

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
        composable(route = HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(route = HistoryScreen.route) {
            HistoryScreen(navController)
        }

        composable(route = Screens.DeviceHistoryScreen.route + "/{deviceId}"){
            DeviceDetailHistoryScreen(navController = navController)
        }

        deviceDetailNavGraph(navController)
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


@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.deviceDetailNavGraph(navController: NavHostController) {
    navigation(
        route = Screens.DeviceDetailNavGraph.route,
        startDestination = Screens.DeviceDetailScreen.route
    ) {

        composable(route = Screens.DeviceDetailScreen.route + "/{deviceId}/{userDeviceId}") {
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { -400 }, animationSpec = tween(400)
                ) + expandHorizontally(
                    expandFrom = Alignment.Start
                ) + fadeIn(initialAlpha = 1f),
                exit = slideOutHorizontally() + shrinkHorizontally() + fadeOut(),
                content = {
                    DeviceDetailScreen(navController)
                },
                initiallyVisible = false
            )

        }

        composable(route = Screens.DeviceDetailConfigScreen.route) {

        }
    }
}
