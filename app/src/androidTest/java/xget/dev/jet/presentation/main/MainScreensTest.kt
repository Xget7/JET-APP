package xget.dev.jet.presentation.main

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import xget.dev.jet.BuildConfig
import xget.dev.jet.MainActivity
import xget.dev.jet.core.di.AppModule
import xget.dev.jet.core.di.RemoteModule
import xget.dev.jet.core.utils.TestTags
import xget.dev.jet.core.utils.TestTags.GO_TO_ADD_DEVICE_STEP_2_BTN
import xget.dev.jet.core.utils.TestTags.SEARCH_DEVICES_STEPPER
import xget.dev.jet.core.utils.TestTags.SMART_DEVICE_HISTORY_ITEM
import xget.dev.jet.core.utils.TestTags.SMART_DEVICE_ITEM
import xget.dev.jet.core.utils.TestTags.USER_WIFI_PASSWORD_TEXT_FIELD
import xget.dev.jet.core.utils.WifiUtil
import xget.dev.jet.presentation.utils.Screens
import javax.inject.Inject

@UninstallModules(AppModule::class, RemoteModule::class)
@HiltAndroidTest
class MainScreensTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Inject
    lateinit var wifiUtil: WifiUtil

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
    )
    @Before
    fun setup() {

        permissions.forEach {
            InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(
                BuildConfig.APPLICATION_ID, it
            )
        }
        hiltRule.inject()
        compRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MainScreens(navController = navController)
        }
    }

    @Test
    fun appNavHost_verifyStartDestination() {
        compRule.onNodeWithTag(TestTags.HOME_SCREEN).assertIsDisplayed()
    }

    @Test
    fun appNavHost_goToAddDeviceScreenAndCompleteAllFields() {
        val deviceNameText = "Porton de Juan"
        val userPasswordText = "Android"

        compRule.onNodeWithTag(TestTags.GO_TO_ADD_DEVICE_SCREEN).performClick()
        val route = navController.currentDestination?.route
        Assert.assertEquals(route, Screens.PairDeviceFirstStep.route)


        //Verify TextField Step 1
        compRule.onNodeWithTag(TestTags.DEVICE_NAME_TEXT_FIELD).performTextInput(deviceNameText)
        compRule.onNodeWithTag(TestTags.DEVICE_NAME_TEXT_FIELD).assert(hasText(deviceNameText)).also {
            Espresso.closeSoftKeyboard()
        }

        compRule.onNodeWithTag(GO_TO_ADD_DEVICE_STEP_2_BTN).performClick()

        compRule.waitUntil {
            compRule
                .onAllNodesWithTag(USER_WIFI_PASSWORD_TEXT_FIELD)
                .fetchSemanticsNodes().size == 1
        }

        Assert.assertEquals(navController.currentDestination?.route, Screens.PairDeviceSecondStep.route)
//
        val wifiName =  wifiUtil.getWifiSSID()
//
        //Write password and verify
        compRule.onNodeWithTag(USER_WIFI_PASSWORD_TEXT_FIELD).performTextInput(userPasswordText)
//        //Verify user wifi name
        compRule.onNodeWithTag(TestTags.USER_WIFI_SSID_TEXT_FIELD).assert(hasText(wifiName))

        //Go to 3rd step
        compRule.onNodeWithTag(TestTags.GO_TO_ADD_DEVICE_STEP_3_BTN).performClick()

        print(navController.currentDestination?.route)

        Assert.assertEquals(navController.currentDestination?.route, Screens.PairDeviceThirdStep.route)



    }

    @Test
    fun navDeviceDetails_goToDeviceDetailsGoToDeviceHistory(){

        compRule.waitUntil(5000) {
            compRule
                .onAllNodesWithTag(SMART_DEVICE_ITEM)
                .fetchSemanticsNodes().size == 1
        }
        compRule.onNodeWithTag(SMART_DEVICE_ITEM).performClick()

        compRule.waitUntil(3000) {
            compRule
                .onAllNodesWithTag(TestTags.DEVICE_DETAIL_SCREEN)
                .fetchSemanticsNodes().size == 1
        }
        compRule.onNodeWithTag(TestTags.DEVICE_DETAIL_SCREEN).assertIsDisplayed()

        Espresso.pressBack()

        compRule.onNodeWithText(BottomBarScreen.History.title).performClick()

        compRule.waitUntil(3000) {
            compRule
                .onAllNodesWithTag(TestTags.SMART_DEVICE_HISTORY_ITEM)
                .fetchSemanticsNodes().size == 1
        }

        compRule.onNodeWithTag(SMART_DEVICE_HISTORY_ITEM).performClick()

        Assert.assertTrue(navController.currentDestination?.route?.contains(Screens.DeviceHistoryScreen.route) ?: false)

        Espresso.pressBack()

    }


}