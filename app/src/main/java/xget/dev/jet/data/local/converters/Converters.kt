package xget.dev.jet.data.local.converters

import androidx.room.TypeConverter
import info.mqtt.android.service.QoS


class Converters {

    @TypeConverter
    fun toQoS(value: Int) = enumValues<QoS>()[value]

    @TypeConverter
    fun fromQoS(value: QoS) = value.value
}