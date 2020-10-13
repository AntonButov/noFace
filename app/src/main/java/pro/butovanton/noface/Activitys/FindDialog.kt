package pro.butovanton.noface.Activitys

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import pro.butovanton.noface.R

class FindDialog(val findDialogAction : FindDialogAction) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.find_chat_dialog, null)

        val mAdView = v.findViewById(R.id.adViewFindDialog) as AdView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        return AlertDialog.Builder(requireActivity())
            .setTitle("Поиск чата...")
            .setView(v)
            .setPositiveButton(
                "Отмена."
            ) { dialog: DialogInterface?, which: Int ->
                findDialogAction.onCancel()
            }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        findDialogAction.onCancel()
    }

}