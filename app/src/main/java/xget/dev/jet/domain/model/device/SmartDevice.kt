package xget.dev.jet.domain.model.device

data class SmartDevice(
    val id : String = "",
    val uid : String = "",
    val name : String = "",
    val online : Boolean = false,
    val stateValue : Int = 0
)
