package xget.dev.jet

import android.app.Application
import android.content.Context
import android.util.Base64
import dagger.hilt.android.HiltAndroidApp
import xget.dev.jet.core.utils.ConstantsShared.AUTH_PREFERENCES
import xget.dev.jet.core.utils.ConstantsShared.jwtKey
import xget.dev.jet.di.DaggerApplicationComponent

@HiltAndroidApp
class JetApp : Application() {


    val appComponent = DaggerApplicationComponent
        .create()

    override fun onCreate() {

        super.onCreate()
        appComponent.inject(this)
        System.loadLibrary("keys");

    }

}