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
import androidx.compose.ui.platform.LocalContext
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
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetBlue

@Composable
fun WelcomeScreen(
    navController: NavController,
) {
    val context = LocalContext.current
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
                    context.getString(R.string.text_welcome),
                    modifier = Modifier,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    Color(
                        0xFFFFFFFF
                    ),
                )
                TextWithShadow(
                    context.getString(R.string.text_jet_iot),
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
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomBackgroundButton(
                        context.getString(R.string.text_button_ingresar),
                        onClick = { navController.navigate(Screens.LoginScreen.route) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomTextButton(
                        context.getString(R.string.text_button_registrarme),
                        onClick = { navController.navigate(Screens.RegisterScreen.route) },
                        textColor = JetBlue,
                    )
                }


            }
            Image(
                imageVector = welcomeScreenBottom(),
                contentDescription = context.getString(R.string.content_description_bottom),
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