package pro.butovanton.noface.di

import android.app.Application
import pro.butovanton.noface.Repo
import javax.inject.Inject

class App : Application() {

    companion object {
        @JvmStatic lateinit var appcomponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
       appcomponent = DaggerAppComponent
           .builder()
           .appModule(AppModule())
           .build()
      }
}