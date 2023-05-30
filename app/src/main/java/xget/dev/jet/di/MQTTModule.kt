package xget.dev.jet.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import info.mqtt.android.service.MqttAndroidClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import xget.dev.jet.core.utils.ConstantsShared.serverUri
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MQTTModule {




    @Provides
    @Singleton
    fun provideHttpClient() : HttpClient{
        return HttpClient(Android){
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
                filter { request ->
                    request.url.host.contains("ktor.io")
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }
    }


    @Provides
    @Singleton
    fun provideMQTTClient(@ApplicationContext c : Context) : MqttAndroidClient{
      return  MqttAndroidClient(c, serverUri, "")
    }


}