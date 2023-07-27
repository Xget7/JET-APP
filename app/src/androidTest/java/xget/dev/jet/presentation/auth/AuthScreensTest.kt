package xget.dev.jet.presentation.auth

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xget.dev.jet.MainActivity
import xget.dev.jet.core.di.AppModule
import xget.dev.jet.core.di.RemoteModule
import xget.dev.jet.core.utils.TestTags.GO_TO_FORGOT_PASSWORD_SCREEN
import xget.dev.jet.core.utils.TestTags.GO_TO_REGISTER_SCREEN
import xget.dev.jet.core.utils.TestTags.LOGIN_SCREEN
import xget.dev.jet.core.utils.TestTags.SHOW_PASSWORD_ICON
import xget.dev.jet.presentation.auth.forgotpassword.sendEmailVerification.ForgotPasswordScreen
import xget.dev.jet.presentation.auth.login.LoginScreen
import xget.dev.jet.presentation.auth.register.RegisterScreen
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.utils.Screens


@UninstallModules(AppModule::class, RemoteModule::class)
@HiltAndroidTest
class AuthScreensTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup(){
        hiltRule.inject()
        compRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            JETTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screens.LoginScreen.route
                ) {
                    composable(route = Screens.LoginScreen.route) {
                        LoginScreen(navController = navController)
                    }
                    composable(route = Screens.RegisterScreen.route) {
                        RegisterScreen(navController = navController)
                    }
                    composable(route = Screens.ForgotPasswordScreen.route) {
                        ForgotPasswordScreen(navController = navController)
                    }
                }
            }
        }
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        compRule.onNodeWithTag(LOGIN_SCREEN).assertIsDisplayed()
    }

    @Test
    fun appNavHost_verifyNavigationToRegisterScreen() {
        compRule.onNodeWithTag(GO_TO_REGISTER_SCREEN).performClick()
        val route = navController.currentDestination?.route
        Assert.assertEquals(route, Screens.RegisterScreen.route)
    }

    @Test
    fun appNavHost_verifyNavigationToForgotPasswordScreen() {
        compRule.onNodeWithTag(GO_TO_FORGOT_PASSWORD_SCREEN).performClick()
        val route = navController.currentDestination?.route
        Assert.assertEquals(route, Screens.ForgotPasswordScreen.route)
    }


    @Test
    fun  clickHintPassword_isVisible(){
        compRule.onNodeWithTag(SHOW_PASSWORD_ICON).performClick()
        compRule.onNodeWithTag(SHOW_PASSWORD_ICON).assertIsDisplayed()
    }


}