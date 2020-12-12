package pro.butovanton.noface.di

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.android.gms.ads.MobileAds
import pro.butovanton.noface.MService
import pro.butovanton.noface.MyFirebaseMessagingService

class App : Application() {

    companion object {

        private lateinit var app : Application

        val TAG = "ANONIM_CHAT"

        @JvmStatic
        lateinit var appcomponent: AppComponent

        fun getApp() : Application {
            return app
        }
    }

    override fun onCreate() {
        super.onCreate()
        appcomponent = DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()

        app = this
        appcomponent.getBilling()
        MobileAds.initialize(this, "ca-app-pub-8158565231911074~2301325206")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d( TAG, "App terminate.")
    }

}