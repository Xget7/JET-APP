@file:Suppress("UNUSED_EXPRESSION")

package xget.dev.jet.presentation.main.home.device_details

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import xget.dev.jet.R
import xget.dev.jet.core.utils.TestTags.DEVICE_DETAIL_SCREEN
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.presentation.main.home.device_details.components.JetCardIcon
import xget.dev.jet.presentation.main.home.device_details.components.ShareDeviceDialog
import xget.dev.jet.presentation.theme.JETTheme
import xget.dev.jet.presentation.theme.JetBlue2
import java.util.Locale

@Composable
fun DeviceDetailScreen(
    navController: NavController,
    viewModel: DeviceDetailViewModel = hiltViewModel()
) {

    val uiState = viewModel.state.collectAsState()
    DeviceDetailScreen(
        uiState,
        onSwitch = viewModel::sendMessageToDevice,
        onBack = navController::navigateUp,
        onDialogAccept = {}

    )

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun DeviceDetailScreen(
    state: State<DeviceDetailUiState>,
    onBack: () -> Unit,
//    onConfig: () -> Unit,
    onSwitch: () -> Unit,
    onDialogAccept: (String) -> Unit
) {
    val dialog = remember {
        mutableStateOf(false)
    }

    val interactionSource = remember { MutableInteractionSource() }

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val active by remember {
        derivedStateOf { state.value.smartDevice.stateValue.intValue == 1 }
    }
    val customWhite = Color(0xFFF6F7FB)
    val customBlack = Color(0xFF2E2E2E)
    val backgroundColor = remember { Animatable(customWhite) }
    val negativeBackgroundColor = remember { Animatable(customWhite) }

    AnimatedVisibility(dialog.value) {
        ShareDeviceDialog(
            {
                onDialogAccept(it)
                dialog.value = false
            }

        ) {
            dialog.value = false
        }
    }


    LaunchedEffect(active) {
        if (active) {
            backgroundColor.animateTo(customWhite, animationSpec = tween(1200))
        } else {
            backgroundColor.animateTo(customBlack, animationSpec = tween(1200))
        }

    }
    LaunchedEffect(active) {
        if (active) {
            negativeBackgroundColor.animateTo(customBlack, animationSpec = tween(1000))
        } else {
            negativeBackgroundColor.animateTo(customWhite, animationSpec = tween(1000))
        }

    }
    val scaffoldState = rememberScaffoldState()

    if (state.value.isLoading) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(color = JetBlue2, modifier = Modifier.size(30.dp))
        }
    }

    AnimatedVisibility(
        visible = !state.value.isLoading,
        enter = expandHorizontally(expandFrom = Alignment.Start, animationSpec = tween(200))
    ) {
        Scaffold(
            modifier = Modifier
                .background(backgroundColor.value)
                .testTag(DEVICE_DETAIL_SCREEN),
            scaffoldState = scaffoldState,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 20.dp), horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back arrow",
                            tint = negativeBackgroundColor.value,
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Text(
                        text = state.value.smartDevice.name.capitalize(Locale.getDefault()),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        color = negativeBackgroundColor.value,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(240.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.settings_icon),
                            contentDescription = "Back arrow",
                            tint = JetBlue2,
                            modifier = Modifier
                                .size(40.dp)

                        )
                    }
                }
            },
            backgroundColor = backgroundColor.value
        ) {
            it
            BoxWithConstraints {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = (screenHeight / 6).dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    AnimatedVisibility(true) {
                        if (active) {
                            Image(
                                painter = painterResource(id = R.drawable.switch_on),
                                contentDescription = "turn on",
                                modifier = Modifier
                                    .size(270.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        onSwitch()
                                    }
                            )

                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.switch_off),
                                contentDescription = "turn off",
                                modifier = Modifier
                                    .size(270.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        onSwitch()
                                    }
                            )

                        }

                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Dispositivo ${if (active) "Encendido" else "Apagado"}",
                        fontWeight = FontWeight.Bold,
                        color = negativeBackgroundColor.value
                    )

                    Spacer(modifier = Modifier.height(40.dp))


                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp, end = 50.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        JetCardIcon(
                            drawable = R.drawable.add_friend_icon,
                            text = " Compartir \n Acceso ",
                            color = negativeBackgroundColor.value,
                            aligmText = TextAlign.Center
                        ) {
                            dialog.value = true
                        }
                        JetCardIcon(
                            drawable = R.drawable.config_time_icon,
                            text = "Configurar \n Timer",
                            color = negativeBackgroundColor.value,
                            aligmText = TextAlign.Center
                        ) {

                        }
                    }
                }
            }
        }
    }


}


@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun DeviceDetailPreview() {

    val mutableState = mutableIntStateOf(1)

    JETTheme {
        DeviceDetailScreen(
            mutableStateOf(
                DeviceDetailUiState(
                    smartDevice = SmartDevice(
                        "23232",
                        "23232",
                        "Switch Wifi",
                        online = true,
                        stateValue = mutableState,
                        type = ""
                    ),
                ),
            ),
            {},
            {}
        ) {
            mutableState.intValue = if (mutableState.intValue == 1) 0 else 1
        }
    }
}

