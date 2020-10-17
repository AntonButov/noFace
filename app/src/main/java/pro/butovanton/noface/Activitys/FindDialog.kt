package pro.butovanton.noface.Activitys

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import pro.butovanton.noface.R
import java.util.*
import java.util.concurrent.TimeUnit


class FindDialog(val findDialogAction: FindDialogAction, val advertDontShow: Boolean) : DialogFragment() {

    private lateinit var postStartingSearching: Disposable
    var isCancel = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.find_chat_dialog, null)

        val mAdView = v.findViewById(R.id.adViewFindDialog) as AdView

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (!isCancel)
                      findDialogAction.startSearching()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

       postStartingSearching = Observable.just(1)
            .delay(5000, TimeUnit.MILLISECONDS)
            .subscribe {
                isCancel = true
                findDialogAction.startSearching()
            }

        val adRequest = AdRequest.Builder().build()
        if (!advertDontShow)
           mAdView.loadAd(adRequest)
        else findDialogAction.startSearching()

        return AlertDialog.Builder(requireActivity())
            .setTitle("Поиск чата...")
            .setView(v)
            .setPositiveButton(
                "Отмена."
            ) { dialog: DialogInterface?, which: Int ->
                postStartingSearching.dispose()
                findDialogAction.onCancel()
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        postStartingSearching.dispose()
        findDialogAction.onCancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        isCancel = true
        postStartingSearching.dispose()
    }

}