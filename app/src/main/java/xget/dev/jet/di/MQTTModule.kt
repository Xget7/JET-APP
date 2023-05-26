package xget.dev.jet.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import info.mqtt.android.service.MqttAndroidClient
import xget.dev.jet.core.utils.ConstantsShared.serverUri
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MQTTModule {


    @Provides
    @Singleton
    fun provideMQTTClient(@ApplicationContext c : Context) : MqttAndroidClient{
      return  MqttAndroidClient(c, serverUri, "")
    }


}