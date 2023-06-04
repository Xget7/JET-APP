package xget.dev.jet.presentation.auth.register

data class RegisterUiState (
    val isError : String? = null,
    val isLoading : Boolean = false,
    val successfulCreated : Boolean = false
)