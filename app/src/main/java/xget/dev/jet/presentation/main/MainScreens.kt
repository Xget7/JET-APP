package xget.dev.jet.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import xget.dev.jet.HomeBottomNav

@Composable
fun MainScreens(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController )
        },
        modifier = Modifier.padding(top = 30.dp)
    ) {it

        HomeBottomNav(navController)
    }
}