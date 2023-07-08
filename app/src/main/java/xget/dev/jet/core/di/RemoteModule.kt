package xget.dev.jet.core.di

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import info.mqtt.android.service.MqttAndroidClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.gson.gson
import kotlinx.serialization.ExperimentalSerializationApi
import xget.dev.jet.core.utils.ConstantsShared.AWS_MQTT_ENDPOINT
import xget.dev.jet.data.remote.devices.mqtt.DevicesMqttServiceImpl
import xget.dev.jet.data.remote.devices.rest.DevicesRemoteServiceImpl
import xget.dev.jet.data.remote.mqtt.MqttFlowClientImpl
import xget.dev.jet.domain.repository.devices.mqtt.DevicesMqttService
import xget.dev.jet.domain.repository.devices.rest.DevicesRemoteService
import xget.dev.jet.domain.repository.token.Token
import xget.dev.jet.domain.services.mqtt.MqttFlowClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {


    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                gson()
            }



            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
                filter { request ->
                    request.url.host.contains("ktor.io")
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }

        return client
    }


    @SuppressLint("HardwareIds")
    @Provides
    @Singleton
    fun provideMqttAndroidClient(@ApplicationContext context: Context): AWSIotMqttManager {
        val ANDROID_ID = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return AWSIotMqttManager(
            ANDROID_ID,
            xget.dev.jet.BuildConfig.AWS_MQTT_ENDPOINT,
        )
    }

    @Provides
    @Singleton
    fun provideMqttClient(
        @ApplicationContext context: Context,
        mqttClient: AWSIotMqttManager
    ): MqttFlowClient {
        return MqttFlowClientImpl(context, mqttClient)
    }

    @Provides
    @Singleton
    fun provideMqttService(mqttFlowClient: MqttFlowClient, token: Token): DevicesMqttService {
        return DevicesMqttServiceImpl(mqttFlowClient, token)
    }

    @Provides
    @Singleton
    fun provideDeviceRemote(httpClient: HttpClient, token: Token): DevicesRemoteService {
        return DevicesRemoteServiceImpl(httpClient, token)
    }

}