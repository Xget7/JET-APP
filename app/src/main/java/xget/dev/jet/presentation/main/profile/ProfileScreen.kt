package xget.dev.jet.presentation.main.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import xget.dev.jet.R
import xget.dev.jet.core.ui.components.AlignedCircularProgressIndicator
import xget.dev.jet.core.ui.components.TextWithShadow
import xget.dev.jet.core.utils.decodeBase64ToBitmap
import xget.dev.jet.presentation.main.profile.components.OptionsCard
import xget.dev.jet.presentation.main.profile.components.ProfileOptionsData
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetScreensBackgroundColor

@Composable
fun ProfileScreen(
    navController: NavController,
    vm: ProfileViewModel = hiltViewModel()
) {
    ProfileScreen(
        uiState = vm.state.collectAsState(),
    )

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
internal fun ProfileScreen(
    uiState: State<ProfileUiState>
) {

    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = Color(0xff006FEE).toArgb()
    }


    val profileGeneralOptions = listOf(
        ProfileOptionsData(
            title = "Configuración del perfil",
            description = "Modifica y actualiza tu perfil",
            id = 1
        ),
        ProfileOptionsData(
            title = "Privacidad",
            description = "Cambia tu contraseña",
            id = 2
        ),
        ProfileOptionsData(
            title = "Dispositivos Compartidos",
            description = "invitaciones para agregar dispositivos compartidos por otros usuarios.",
            id = 3
        ),
    )

    val profileSupportOptions = listOf(
        ProfileOptionsData(
            title = "Sobre nostros",
            description = "Conoce mas sobre nuetro equipo y objetivos",
            id = 4
        ),
        ProfileOptionsData(
            title = "Contacto",
            description = "Envianos un mensaje via gmail",
            id = 5
        ),
    )

    val scaffoldState = rememberScaffoldState()


    if (uiState.value.isLoading) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlignedCircularProgressIndicator()
        }

    } else {
        Scaffold(
            Modifier
                .fillMaxSize()
                .background(JetScreensBackgroundColor)
                .padding(bottom = 55.dp),
            scaffoldState = scaffoldState,
            topBar = {
                Image(
                    painter = painterResource(id = R.drawable.profile_background),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                        .padding(top = 35.dp)
                ) {
                    Column(
                        Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextWithShadow(
                            text = "Mi Perfil",
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold,
                            shadow = true,
                            color = Color.White,
                            fontSize = 30.sp
                        )


                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                Color.Transparent,
                                contentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            elevation = CardDefaults.cardElevation(8.dp),
                            modifier = Modifier.background(Color.Transparent),
                        ) {
                            if (uiState.value.user.profilePicture.isNullOrEmpty() || uiState.value.user.profilePicture == "null") {
                                Image(
                                    painter = painterResource(R.drawable.profle_pic_placeholder),
                                    contentDescription = "Profile Photo",
                                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                                    modifier = Modifier
                                        .size(144.dp)
                                        .clip(CircleShape)                       // clip to the circle shape
                                )
                            } else {
                                var image: Bitmap? = null
                                try {
                                    image =
                                        decodeBase64ToBitmap(uiState.value.user.profilePicture!!)
                                } catch (e: Exception) {
                                    image = null
                                }
                                if (image == null) {
                                    Image(
                                        painter = painterResource(R.drawable.profle_pic_placeholder),
                                        contentDescription = "Profile Photo",
                                        contentScale = ContentScale.Crop,            // crop the image if it's not a square
                                        modifier = Modifier
                                            .size(144.dp)
                                            .clip(CircleShape)                       // clip to the circle shape
                                    )
                                } else {
                                    Image(
                                        bitmap = image!!.asImageBitmap(),
                                        contentDescription = "Profile Photo",
                                        contentScale = ContentScale.Crop,            // crop the image if it's not a square
                                        modifier = Modifier
                                            .size(144.dp)
                                            .clip(CircleShape)                       // clip to the circle shape // add a border (optional)
                                    )
                                }
                            }

                        }


                        TextWithShadow(
                            text = uiState.value.user.userName,
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold,
                            shadow = false,
                            color = Color(0xFF435080),
                            fontSize = 30.sp
                        )
                    }
                }
            },
            backgroundColor = JetScreensBackgroundColor,
        ) {
            it

            LaunchedEffect(uiState.value.isError) {
                if (uiState.value.isError != null) {
                    Log.d("luanchEffect", "isError ${uiState.value.isError}")
                    scaffoldState.snackbarHostState.showSnackbar(
                        uiState.value.isError ?: "Error Inesperado",
                        duration = SnackbarDuration.Short
                    )
                }
            }



            Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 26.dp)) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    TextWithShadow(
                        text = "General",
                        modifier = Modifier,
                        fontWeight = FontWeight.Medium,
                        shadow = false,
                        fontSize = 22.sp,
                        color = Color(0xff000000).copy(alpha = 0.4f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OptionsCard(options = profileGeneralOptions, onClicked = {
                    })
                    Spacer(modifier = Modifier.height(12.dp))

                    TextWithShadow(
                        text = "Soporte",
                        modifier = Modifier,
                        fontWeight = FontWeight.Medium,
                        shadow = false,
                        fontSize = 22.sp,
                        color = Color(0xff000000).copy(alpha = 0.4f)
                    )

                    OptionsCard(options = profileSupportOptions, onClicked = {
                    })
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }


        }
    }


}


@SuppressLint("UnrememberedMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPrev() {
    JETTheme {
        ProfileScreen(uiState = mutableStateOf(ProfileUiState()))
    }
}