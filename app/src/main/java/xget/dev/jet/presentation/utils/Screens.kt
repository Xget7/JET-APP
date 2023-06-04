package xget.dev.jet.presentation.utils

sealed class Screens(val route : String){

    object WelcomeScreen : Screens("welcome_screen")
    object LoginScreen : Screens("login_screen")
    object RegisterScreen : Screens("register_screen")
    object ForgotPasswordScreen : Screens("forgot_password_screen")
    object EmailSentScreen : Screens("email_sent_screen")

}

