package xget.dev.jet.data.repository.devices

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.coroutineContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.model.mqtt.SmartDeviceMqtt
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import javax.inject.Inject

class DevicesRepositoryImpl @Inject constructor(
    private val deviceService: DevicesRemoteService,
    private val mqttDevices: DevicesMqttService
) : DevicesRepository {


    override suspend fun getDevice(uid: String) = callbackFlow<SmartDevice> {


    }.flowOn(Dispatchers.IO)

    override fun getDevicesByUserId(uid: String): Flow<List<SmartDevice>> =
        flow {

            Log.d("called ", "getDevicesByUserId")
            //MOCK
            val restDevices = listOf(
                DeviceDto(
                    "7821849C1782",
                    "Porton Casita",
                    uid,
                    "GATE",
                    listOf()
                )
            )

            //REAL
            //deviceService.getDevicesByUserUid(uid)
//        if (restDevices != null) {
//            emit(emptyList())
//        }
            mqttDevices.subscribeAndCollectDevices(restDevices.map { it.id }).collect { mqttList ->
                emit(mqttList.zip(restDevices).convertToSmartDevice())
            }
        }


}

fun List<Pair<SmartDeviceMqtt, DeviceDto>>.convertToSmartDevice(): List<SmartDevice> {
    val tempList = mutableListOf<SmartDevice>()
    for (i in this) {
        tempList.add(
            SmartDevice(
                i.second.id, i.second.userId, i.second.name, i.first.online == 1,
                mutableIntStateOf(i.first.state)
            )
        )
    }
    return tempList
}