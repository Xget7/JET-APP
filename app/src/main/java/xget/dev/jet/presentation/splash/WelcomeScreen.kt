package xget.dev.jet.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.CustomBackgroundButton
import xget.dev.jet.core.ui.components.CustomTextButton
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.presentation.splash.components.jetvectorsandicons.Vectors.welcomeScreenBottom
import xget.dev.jet.presentation.splash.components.jetvectorsandicons.Vectors.welcomeScreenTopVector
import xget.dev.jet.presentation.utils.Screens
import xget.dev.jet.ui.theme.JETTheme
import xget.dev.jet.ui.theme.JetBlue

@Composable
fun WelcomeScreen(
    navController: NavController,
) {

    Box() {

        Image(imageVector = welcomeScreenTopVector(), contentDescription = "Top")

        Box(
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .padding(top = 30.dp)
                    .fillMaxSize()
            ) {

                TextWithShadow(
                    "Bienvenido",
                    modifier = Modifier,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    Color(
                        0xFFFFFFFF
                    ),
                )
                TextWithShadow(
                    "Jet IOT",
                    modifier = Modifier,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    Color(
                        0xFF435080
                    ),
                )


                Image(
                    painter = painterResource(id = R.drawable.welcome_screen_people_flying),
                    contentDescription = " Fliying people",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(400.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomBackgroundButton(
                        "Ingresar",
                        onClick = { navController.navigate(Screens.LoginScreen.route) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomTextButton(
                        "Registrarme",
                        onClick = { navController.navigate(Screens.RegisterScreen.route) },
                        textColor = JetBlue,
                    )
                }


            }
            Image(
                imageVector = welcomeScreenBottom(),
                contentDescription = "Welcome Screen Bottom draw",
                modifier = Modifier.align(Alignment.BottomStart)
            )

        }


    }


}

@Preview
@Composable
fun WelcomeScreenPreview() {
    JETTheme {
        WelcomeScreen(navController = rememberNavController())
    }
}