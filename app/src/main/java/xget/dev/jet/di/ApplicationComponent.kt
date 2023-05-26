package xget.dev.jet.di;

import dagger.Component;
import xget.dev.jet.JetApp

@Component(
        modules =[AppModule::class , RoomModule::class, MQTTModule::class]
)
interface ApplicationComponent {
    fun inject(app : JetApp)
}