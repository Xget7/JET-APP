package xget.dev.jet.presentation.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import xget.dev.jet.MainActivity
import xget.dev.jet.R
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.core.utils.ConstantsShared.IsFirstTime
import xget.dev.jet.data.util.token.TokenImpl
import xget.dev.jet.presentation.auth.forgotpassword.emailSent.EmailSentScreen
import xget.dev.jet.presentation.auth.forgotpassword.sendEmailVerification.ForgotPasswordScreen
import xget.dev.jet.presentation.auth.login.LoginScreen
import xget.dev.jet.presentation.auth.register.RegisterScreen
import xget.dev.jet.presentation.splash.WelcomeScreen
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.presentation.theme.JETTheme

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPreference: SharedPreferences = getSharedPreferences(
            ConstantsShared.AUTH_PREFERENCES, Context.MODE_PRIVATE
        )
        val token = TokenImpl(this).getJwtLocal()
        setContent {
            JETTheme {

                if (sharedPreference.getBoolean(IsFirstTime, true)) {
                    AuthNavigation(Screens.WelcomeScreen.route)
                    sharedPreference.edit().putBoolean(IsFirstTime, false).apply()
                } else {
//                    AuthNavigation(Screens.LoginScreen.route)
//
                    if (token.isNullOrBlank()) {
                        Log.d("token?","")
                    AuthNavigation(Screens.LoginScreen.route)
                    }else {
                        Log.d("token?","yes? $token")
                        navigateToMain()
                    }


                }
            }
        }
    }

     fun navigateToMain() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}

@Composable
fun AuthNavigation(startDestination: String) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = startDestination) {
        composable(Screens.WelcomeScreen.route) {
            WelcomeScreen(nav)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(nav)
        }
        composable(Screens.RegisterScreen.route) {
            RegisterScreen(nav)
        }
        composable(Screens.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(nav)
        }
        composable(Screens.EmailSentScreen.route) {
            EmailSentScreen(nav)
        }
    }
}



