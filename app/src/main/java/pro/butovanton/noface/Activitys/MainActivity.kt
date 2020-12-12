package pro.butovanton.noface.Activitys

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.*
import com.google.android.material.navigation.NavigationView
import pro.butovanton.noface.MService
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App


class MainActivity : AppCompatActivity() {

    val INTERSTILIAN_SPLASH = "ca-app-pub-8158565231911074/3826954398"
    val INTERSTILIAN_CHAT =   "ca-app-pub-8158565231911074/5118511494"

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var mInterstitialAdSplash: InterstitialAd? = null
    private var mInterstitialAdChat: InterstitialAd? = null
  //  private lateinit var firstDialog : FirstDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val ver = packageInfo!!.versionName
        val verText : TextView = navView.getHeaderView(0).findViewById(R.id.verText)
        verText.text = "Анонимный чат, версия " + ver

 //       firstDialog =  FirstDialog.newInstance(Bundle())
 //       firstDialog.isCancelable = false
  //      firstDialog.show(supportFragmentManager, "firstfragment")

        mInterstitilAdInitSplash()
        mInterstitilAdInitChat()
        val billing = (App).appcomponent.getBilling()
        if (!billing.isAdvertDontShow())
            showInterAdwertSplash()

        val intentMService = Intent( this, MService::class.java)
        startService(intentMService)
    }

    private fun showInterAdwertSplash() {
        mInterstitialAdSplash!!.loadAd(AdRequest.Builder().build())
    }

    fun showInterAdwertChat() {
        mInterstitialAdChat!!.loadAd(AdRequest.Builder().build())
    }

    private fun mInterstitilAdInitSplash() {
        mInterstitialAdSplash = InterstitialAd(application)
        mInterstitialAdSplash!!.adUnitId = INTERSTILIAN_SPLASH
        //mInterstitialAd!!.adUnitId = "ca-app-pub-3940256099942544/1033173712" // test

        mInterstitialAdSplash!!.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d((App).TAG, "AdLoad ")

                if (mInterstitialAdSplash != null
                    && mInterstitialAdSplash!!.isLoaded()
                ) {
                    mInterstitialAdSplash!!.show();
                    Log.d((App).TAG, " Ad show")
                } else {
                    Log.d((App).TAG, " Ad not loaded")
                }

            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                println(adError)
                Log.d((App).TAG, "AdError " + adError.message)
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        }
    }
    private fun mInterstitilAdInitChat() {
        mInterstitialAdChat = InterstitialAd(application)
        mInterstitialAdChat!!.adUnitId = INTERSTILIAN_CHAT
        //mInterstitialAd!!.adUnitId = "ca-app-pub-3940256099942544/1033173712" // test

            mInterstitialAdChat!!.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    Log.d((App).TAG, "AdLoad ")

                    if (mInterstitialAdChat != null
                        && mInterstitialAdChat!!.isLoaded()
                    )
                    {
                        mInterstitialAdChat!!.show();
                        Log.d((App).TAG, " Ad show" )
                    } else {
                        Log.d((App).TAG, " Ad not loaded" )
                    }

                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    println(adError)
                    Log.d((App).TAG, "AdError " + adError.message)
                    // Code to be executed when an ad request fails.
                }

                override fun onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    // Code to be executed when the interstitial ad is closed.
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
     //   menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

