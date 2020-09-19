package pro.butovanton.noface.Activitys

import android.content.DialogInterface
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.nav_header_main.*
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var consept = true

    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_advert, R.id.nav_setting
            ), drawerLayout
        )
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

            var firstDialog =  AlertDialog.Builder(this)
                .setTitle("Правила чата.")
                .setMessage(getResources().getString(R.string.rules))
                .setPositiveButton("Согласен", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        consept = true
                    }
                })
                .create()
        firstDialog.setOnCancelListener {
            finish()
        }
        firstDialog.show()

        MobileAds.initialize(this)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = "ca-app-pub-8158565231911074/5118511494"
            //    mInterstitialAd!!.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd!!.loadAd(AdRequest.Builder().build())

        mInterstitialAd!!.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.d((App).TAG, "AdLoad ")
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

    fun showAdwert() {

        if (mInterstitialAd != null
            && mInterstitialAd!!.isLoaded()
        )
        {
            mInterstitialAd!!.show();
        } else {
   //         Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
           // startGame();
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

