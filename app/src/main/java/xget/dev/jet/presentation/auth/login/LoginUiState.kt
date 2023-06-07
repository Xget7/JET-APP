package xget.dev.jet.presentation.auth.login

data class LoginUiState(
    val isError : String? = null,
    val isLoading : Boolean = false,
    val isLoggedIn : Boolean = false
)
