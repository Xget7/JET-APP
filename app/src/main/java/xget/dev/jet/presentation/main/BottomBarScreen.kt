package xget.dev.jet.presentation.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import xget.dev.jet.R
import xget.dev.jet.core.utils.TestTags
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.theme.JetGray2

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Home : BottomBarScreen(
        route = Screens.HomeScreen.route,
        title = "Home",
        icon = R.drawable.bottom_nav_home
    )

    object Profile : BottomBarScreen(
        route = Screens.ProfileScreen.route,
        title = "Yo",
        icon = R.drawable.bottom_nav_profile
    )

    object History : BottomBarScreen(
        route = Screens.HistoryScreen.route,
        title = "Historial",
        icon = R.drawable.bottom_nav_history
    )
}


@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true


    BottomNavigationItem(
        label = {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = screen.title,
                fontSize = 13.sp,
                color = if (isSelected) Color(0xFF1F7EFF) else JetGray2
            )
        },
        icon = {

            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Navigation Icon",
                tint = if (!isSelected) JetGray2 else Color(0xFF1F7EFF)
            )
        },
        modifier = Modifier,
        selected = isSelected,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.History,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier.height(60.dp),
            elevation = 4.dp
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}