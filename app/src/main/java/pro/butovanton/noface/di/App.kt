package pro.butovanton.noface.di

import android.app.Application
import android.os.Build
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.core.provider.FontRequest
import android.util.Log
import androidx.annotation.RequiresApi
import pro.butovanton.noface.R

class App : Application() {

    companion object {
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