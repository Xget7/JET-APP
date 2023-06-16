package xget.dev.jet.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import xget.dev.jet.MainActivity
import xget.dev.jet.domain.repository.auth.AuthRepository
import xget.dev.jet.presentation.auth.AuthActivity
import xget.dev.jet.presentation.theme.JETTheme
import javax.inject.Inject


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    @Inject
    lateinit var repo : AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            JETTheme() {
                Box(){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "JET IOT", fontSize = 44.sp)
                    }

                }
            }

        }

        getUserAndNavigate(repo.isLoggedIn())
    }


    private fun getUserAndNavigate(isLoggedIn: Boolean?) {
        lifecycleScope.launch {
            navigateAuth()
//            if (isLoggedIn == false){
//                navigateAuth()
//            }else{
//                navigateMain()
//            }
        }
    }
    private fun navigateMain(){
        val i = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }
    private fun navigateAuth(){
        val i = Intent(this@SplashActivity, AuthActivity::class.java)
        startActivity(i)
        finish()
    }

}