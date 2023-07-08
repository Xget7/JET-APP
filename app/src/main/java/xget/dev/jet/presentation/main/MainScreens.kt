package xget.dev.jet.presentation.main

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import xget.dev.jet.HomeBottomNav
import xget.dev.jet.presentation.main.home.device_details.DeviceDetailScreen
import xget.dev.jet.presentation.utils.Screens

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreens(navController: NavHostController) {


    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically (
                    initialOffsetY = { -400 }, animationSpec = tween(400)
                ),
                exit = slideOutHorizontally( animationSpec = tween(700)),
                content = {
                    BottomBar(navController = navController)
                },
                initiallyVisible = false
            )
        },
        modifier = Modifier.padding(top = 0.dp)
    ) { it
        HomeBottomNav(navController)
    }
}