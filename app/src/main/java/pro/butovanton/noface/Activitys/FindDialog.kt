package pro.butovanton.noface.Activitys

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pro.butovanton.noface.R

class FindDialog(val findDialogAction : FindDialogAction) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.find_chat_dialog, null)
        return AlertDialog.Builder(requireActivity())
            .setTitle("Поиск чата...")
            .setView(v)
            .setPositiveButton(
                "Отмена."
            ) { dialog: DialogInterface?, which: Int ->
                findDialogAction.onCancel()
                dismiss() }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        findDialogAction.onCancel()
    }

}