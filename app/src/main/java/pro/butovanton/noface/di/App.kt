package pro.butovanton.noface.di

import android.app.Application
import android.content.Intent
import android.util.Log
import pro.butovanton.noface.MService
import pro.butovanton.noface.MyFirebaseMessagingService

class App : Application() {

    companion object {

        val TAG = "ANONIM_CHAT"

        @JvmStatic
        lateinit var appcomponent: AppComponent

    }

    override fun onCreate() {
        super.onCreate()
        appcomponent = DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()

    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d( TAG, "App terminate.")
    }

}