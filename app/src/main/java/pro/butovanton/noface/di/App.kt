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

        private val TAG = "EmojiCompatApplication"

        /** Change this to `false` when you want to use the downloadable Emoji font.  */
        private val USE_BUNDLED_EMOJI = true

    }

    override fun onCreate() {
        super.onCreate()
        appcomponent = DaggerAppComponent
            .builder()
            .appModule(AppModule())
            .build()

        val config: EmojiCompat.Config
        if (USE_BUNDLED_EMOJI) {
            // Use the bundled font for EmojiCompat
            config = BundledEmojiCompatConfig(applicationContext)
        } else {
            // Use a downloadable font for EmojiCompat
            val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs
            )
            config = FontRequestEmojiCompatConfig(applicationContext, fontRequest)
                .setReplaceAll(true)
                .registerInitCallback(object : EmojiCompat.InitCallback() {
                    override fun onInitialized() {
                        Log.i(TAG, "EmojiCompat initialized")
                    }

                    override fun onFailed(throwable: Throwable?) {
                        Log.e(TAG, "EmojiCompat initialization failed", throwable)
                    }
                })
        }
        EmojiCompat.init(config)
    }
}