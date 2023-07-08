package xget.dev.jet.presentation.main.home

import kotlinx.coroutines.flow.MutableStateFlow
import xget.dev.jet.domain.model.device.SmartDevice

data class HomeUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val myDevices : MutableStateFlow<List<SmartDevice>> = MutableStateFlow(mutableListOf()),
    val otherUsersDevices : MutableStateFlow<List<SmartDevice>> = MutableStateFlow(mutableListOf())

)
