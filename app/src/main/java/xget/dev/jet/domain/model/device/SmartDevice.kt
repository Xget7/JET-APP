package xget.dev.jet.domain.model.device

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf

data class SmartDevice(
    val id : String = "",
    val uid : String = "",
    val name : String = "",
    val type : String = "",
    var online : Boolean = false,
    val stateValue : MutableIntState = mutableIntStateOf(0)
)
