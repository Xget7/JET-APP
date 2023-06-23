package xget.dev.jet.presentation.main.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.MutableSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import xget.dev.jet.domain.model.device.SmartDevice

data class HomeUiState(
    val isLoading : Boolean = false,
    val isError : String? = null,
    val devices : List<SmartDevice> = mutableListOf()
)
