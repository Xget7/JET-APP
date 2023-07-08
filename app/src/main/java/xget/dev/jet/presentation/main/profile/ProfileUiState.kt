package xget.dev.jet.presentation.main.profile

import kotlinx.coroutines.flow.MutableStateFlow
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.model.user.User

data class ProfileUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val user : User = User()

)
