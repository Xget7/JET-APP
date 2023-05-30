package xget.dev.jet.presentation.auth

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import xget.dev.jet.MainActivity
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.core.utils.ConstantsShared.IsFirstTime
import xget.dev.jet.presentation.auth.login.LoginScreen
import xget.dev.jet.presentation.splash.WelcomeScreen
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.ui.theme.JETTheme

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val sharedPreference: SharedPreferences =  getSharedPreferences(
            ConstantsShared.AUTH_PREFERENCES, Context.MODE_PRIVATE)

        setContent {
            JETTheme {
                if(sharedPreference.getBoolean(IsFirstTime,true)){
                    AuthNavigation(Screens.WelcomeScreen.route)
                    sharedPreference.edit().putBoolean(IsFirstTime,false).apply()
                }else{
                    AuthNavigation(Screens.LoginScreen.route)
                }
            }
        }
    }

    fun navigateToMain() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}

@Composable
fun AuthNavigation(startDestination : String) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination =startDestination ){
        composable(Screens.WelcomeScreen.route){
            WelcomeScreen(nav)
        }
        composable(Screens.LoginScreen.route){
            LoginScreen(nav)
        }
        composable(Screens.RegisterScreen.route){

        }
    }
}



