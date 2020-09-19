package pro.butovanton.noface.di

import android.app.Application

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
}