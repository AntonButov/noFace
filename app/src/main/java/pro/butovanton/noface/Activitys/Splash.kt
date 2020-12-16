package pro.butovanton.noface.Activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.firsrt_dialog_view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App

class Splash : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firsrt_dialog_view)

        val billing = (App).appcomponent.getBilling()

        val mAdView = findViewById(R.id.adViewSplash) as AdView
        val adRequest = AdRequest.Builder().build()
        if (!billing.isAdvertDontShow()) {
            mAdView.loadAd(adRequest)
            mAdView.visibility = View.VISIBLE
        }
        else
            mAdView.visibility = View.GONE

        val linckTV = findViewById(R.id.link_text_view) as TextView
        linckTV.setOnClickListener {
            val url = "http://noface.best/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

    lifecycleScope.launchWhenResumed {
        delay(5000)
        buttonAcept.visibility = View.VISIBLE
    }
        buttonAcept.setOnClickListener {
        it.visibility = View.GONE;
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
        }
    }

}