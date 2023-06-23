package xget.dev.jet.domain.model.mqtt

data class ReceivedMessage(
    val connection : Boolean = true,
    val message : String ="" ,
    val topic : String = "",
    val error : String? = null
)

