package xget.dev.jet.core.di;

import dagger.Component;
import xget.dev.jet.JetApp

@Component(
        modules =[AppModule::class , RoomModule::class, RemoteModule::class]
)
interface ApplicationComponent {
    fun inject(app : JetApp)
}