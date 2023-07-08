package xget.dev.jet.data.repository.devices

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import xget.dev.jet.core.utils.toSmartDevice
import xget.dev.jet.data.remote.devices.rest.dto.DeviceDto
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import javax.inject.Inject

class DevicesRepositoryImpl @Inject constructor(
    private val deviceService: DevicesRemoteService,
    private val mqttDevices: DevicesMqttService
) : DevicesRepository {


    override fun initRepo(): Flow<Boolean> {
        return mqttDevices.init()
    }

    override suspend fun releaseMqtt() {
        mqttDevices.release()
    }

//  TODO("make Drop Down elipsis icon photo in iotin chat")

    private val restDevices = mutableListOf<DeviceDto>()
    private val smartDevices = mutableListOf<SmartDevice>()
    override fun getDevicesByUserId(uid: String): Flow<List<SmartDevice>> = flow {
        //MOCK
        var testedOnline = false
        deviceService.getDevicesByUser().collect { response ->
            val data = response.data
            data?.myDevices?.let {
                restDevices.addAll(it)
            }
            if (data == null) {
                restDevices.addAll(
                    emptyList()
                )
            }


        }

        mqttDevices.subscribeAndCollectDevices(restDevices.map { it.id }, uid)
            .collect { mqttList ->
                if (mqttList.isEmpty()) {
                    //send if device is online
                    if (!testedOnline) {
                        sendAliveMessageToDevices(restDevices.map { it.id }, uid)
                        testedOnline = true
                    }
                    emit(restDevices.toSmartDevice())
                } else {

                    for (deviceDto in restDevices) {
                        val deviceExist = !smartDevices.none { it.id == deviceDto.id }
                        if (!deviceExist) {
                            val pairMqttDevice = mqttList.firstOrNull { it.first == deviceDto.id }
                            smartDevices.add(
                                SmartDevice(
                                    id = deviceDto.id,
                                    uid = deviceDto.userId,
                                    name = deviceDto.name,
                                    type = deviceDto.deviceType,
                                    online = if (pairMqttDevice == null) false else pairMqttDevice.second.online == 1,
                                    stateValue = mutableIntStateOf(
                                        pairMqttDevice?.second?.state
                                            ?: 0
                                    )
                                )
                            )
                        }
                        if (deviceExist) {
                            //if device exist
                            val pairMqttDevice = mqttList.firstOrNull { it.first == deviceDto.id }

                            val deviceIndex = smartDevices.indexOfFirst { it.id == deviceDto.id }

                            if (pairMqttDevice != null) {
                                val updatedDevice = smartDevices[deviceIndex].copy(
                                    online = pairMqttDevice.second.online == 1,
                                    stateValue = mutableIntStateOf(pairMqttDevice.second.state)
                                ) // Update the desired field with the new value
                                smartDevices[deviceIndex] =
                                    updatedDevice // Update the element at the specified index
                            }
                            emit(smartDevices)
                        }
                    }
                    emit(smartDevices)
                }
            }
    }.flowOn(Dispatchers.IO)


    private suspend fun sendAliveMessageToDevices(devices: List<String>, userId: String) {
        for (i in devices) {
            mqttDevices.sendMessageToDevice(i, "3", userId)
            delay(100)
        }
    }

    //We use first time to get previus device state and after we start collecting one device state
    var first = true
    override fun getDevice(deviceId: String, userId: String): Flow<SmartDevice> = flow {

        val restDevice = restDevices.firstOrNull { it.id == deviceId }
        val mqttDeivce2 : SmartDevice? = smartDevices.firstOrNull { it.id == deviceId }
        var emitDevice: SmartDevice? = null

        Log.d("restDeviceGetBySolo", restDevice.toString())
        if (restDevice != null) {
            mqttDevices.subscribeAndCollectOneDevice(deviceId, userId).collect { mqttDevice ->
                emitDevice = SmartDevice(
                    restDevice.id,
                    restDevice.userId,
                    restDevice.name,
                    online = mqttDevice.second.online == 1,
                    stateValue = mutableIntStateOf(mqttDevice.second.state)
                )
                //when repossitory context
//                if not repository context
//                if (!first && mqttDeivce2 == null){
                    emit(emitDevice!!)
//                }else{
//                    emit(mqttDeivce2!!)
//                    first = false
//                }

            }

        } else {
            throw Exception("No se pudo obtener el dispositivo.")
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun sendMessageToDevice(
        deviceId: String,
        state: Int,
        userId: String
    ): Boolean {
        return mqttDevices.sendMessageToDevice(deviceId, state.toString(), userId)
    }


}

