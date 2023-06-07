package xget.dev.jet.presentation.auth.forgotpassword.sendEmailVerification

data class ForgotPasswordUiState (
    val isError : String? = null,
    val isLoading : Boolean = false,
    val emailSent : Boolean = false
)