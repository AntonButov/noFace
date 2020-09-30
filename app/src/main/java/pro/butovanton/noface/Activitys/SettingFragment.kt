package pro.butovanton.noface.Activitys

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App.Companion.TAG


class SettingFragment : PreferenceFragmentCompat() {

    lateinit var shared : SharedPreferences
    var onPause = false

    var mListener =
        OnSharedPreferenceChangeListener { shared, key ->
            Log.d(
                TAG,
                "A preference has been changed"
            )
            GlobalScope.launch(Dispatchers.Main) {
                delay(3000)
              if (onPause == false) requireActivity().onBackPressed()
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref, rootKey)

             shared = PreferenceManager.getDefaultSharedPreferences(context)
        shared.registerOnSharedPreferenceChangeListener(mListener)

    }

    override fun onResume() {
        super.onResume()
        onPause = false
    }

    override fun onPause() {
        super.onPause()
        onPause = true
    }

    override fun onDestroy() {
        super.onDestroy()
        shared.unregisterOnSharedPreferenceChangeListener(mListener)
    }

}

