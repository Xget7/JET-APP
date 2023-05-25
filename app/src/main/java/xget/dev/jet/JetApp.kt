package xget.dev.jet

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import dagger.hilt.android.HiltAndroidApp
import xget.dev.jet.di.DaggerApplicationComponent

@HiltAndroidApp
class JetApp : Application() {


    val appComponent = DaggerApplicationComponent
        .create()


    override fun onCreate() {

        super.onCreate()
        appComponent.inject(this)


    }
}