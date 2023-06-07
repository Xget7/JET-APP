package xget.dev.jet.presentation.utils

sealed class Screens(val route : String){


    //Auth and Init
    object WelcomeScreen : Screens("welcome_screen")
    object LoginScreen : Screens("login_screen")
    object RegisterScreen : Screens("register_screen")
    object ForgotPasswordScreen : Screens("forgot_password_screen")
    object EmailSentScreen : Screens("email_sent_screen")

    //Home , History and profile

    object HomeNavGraph : Screens("home_nav_graph")

    object HomeScreen : Screens("home_screen")
    object HistoryScreen : Screens("history_screen")
    object ProfileScreen : Screens("profile_screen")

    //Bluetooth pair screens

    object BluetoothNavGraph : Screens("bluetooth_nav_graph")
    object PairDeviceFirstStep : Screens("pair_first_step")
    object PairDeviceSecondStep : Screens("pair_second_step")



}

