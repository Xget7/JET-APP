package xget.dev.jet.data.repository.devices

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import xget.dev.jet.domain.model.device.SmartDevice
import xget.dev.jet.domain.repository.devices.DevicesRepository
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Inject

class DevicesRepositoryImpl @Inject constructor(
    private val deviceService : DevicesRemoteService,
    private val mqttClient : MqttFlowClient
) : DevicesRepository {



    override suspend fun getDevice(uid: String) = callbackFlow<SmartDevice> {



    }.flowOn(Dispatchers.IO)

    override suspend fun getDevicesByUserId(uid: String) = callbackFlow<List<SmartDevice>> {
        TODO("Not yet implemented")
    }


}