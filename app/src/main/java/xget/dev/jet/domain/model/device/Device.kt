package xget.dev.jet.domain.model.device

data class Device(
    val id : String,
    val uid : String,
    val name : String,
    val state : Boolean,
    val stateValue : Int
)
