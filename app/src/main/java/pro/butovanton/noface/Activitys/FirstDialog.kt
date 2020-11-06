package pro.butovanton.noface.Activitys

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pro.butovanton.noface.R


class FirstDialog() : DialogFragment() {

    companion object {
        fun newInstance(bundle: Bundle): FirstDialog {
            val fragment = FirstDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.firsrt_dialog_view, null)
    /*
                val text = "Test text to show ClickableSpan example." +
                " Click to display @Red, @Green, @Blue background."
        val ssBuilder = SpannableStringBuilder(text)
        val redClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Log.d("DEBUG", "click red")
            }
        }
        ssBuilder.setSpan(
            redClickableSpan, // Span to add
            text.indexOf("@Red"), // Start of the span (inclusive)
            text.indexOf("@Red") + 4, // End of the span (exclusive)
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
        )
        val linckTV = v.findViewById(R.id.rools_text_view) as TextView
            linckTV.text = ssBuilder
        linckTV.setMovementMethod(LinkMovementMethod.getInstance());
     */
       val linckTV = v.findViewById(R.id.link_text_view) as TextView
        linckTV.setOnClickListener {
            val url = "http://noface.best/"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
       return AlertDialog.Builder(requireActivity())
            .setTitle("Правила чата.")
            .setView(v)
            .setPositiveButton("Согласен") { dialog: DialogInterface?, which: Int -> }
            .setCancelable(false)
            .setOnCancelListener {

           }
            .create()
    }

}