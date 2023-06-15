package xget.dev.jet.presentation.utils



object DevicesTypeObj{
    enum class DevicesType {
        ALARM,
        GATE,
        ELECTRIC_FENCE,
        AUTOMATION
    }

    val listOfDevices = listOf("Alarma","Portón","Cerca Eléctrica", "Automatización")

    fun getDeviceTypes(): Set<DevicesType> {
        val deviceTypes = mutableSetOf<DevicesType>()

        for (device in listOfDevices) {
            when (device) {
                "Alarma" -> deviceTypes.add(DevicesType.ALARM)
                "Portón" -> deviceTypes.add(DevicesType.GATE)
                "Cerca Eléctrica" -> deviceTypes.add(DevicesType.ELECTRIC_FENCE)
                "Automatización" -> deviceTypes.add(DevicesType.AUTOMATION)
            }
        }

        return deviceTypes
    }
}