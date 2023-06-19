package xget.dev.jet.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import xget.dev.jet.core.di.ApplicationComponent
import xget.dev.jet.core.utils.ConstantsShared
import xget.dev.jet.data.remote.mqtt.MqttFlowClientImpl
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideMqttAndroidClient(@ApplicationContext context: Context) : MqttAndroidClient {
        return MqttAndroidClient(context, ConstantsShared.MQTT_BROKER_ADDRESS, ConstantsShared.MQTT_CLIENT_ID)
    }
    @Provides
    fun provideMqttFlow(mqttAndroidClient: MqttAndroidClient) : MqttFlowClient {
        return MqttFlowClientImpl(mqttAndroidClient)
    }

    @Provides
    fun provideTestDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined


}