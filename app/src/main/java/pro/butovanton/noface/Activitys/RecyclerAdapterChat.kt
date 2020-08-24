package pro.butovanton.noface.Activitys

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.R
import java.text.SimpleDateFormat


class RecyclerAdapterChat(val messages: MutableList<Massage>) :
    RecyclerView.Adapter<RecyclerAdapterChat.ViewHolderItemChat>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemChat {
        var view : View

         view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recicler,
            parent,
            false
        )

        return ViewHolderItemChat(
            view
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolderItemChat,
        position: Int
    ) {

        holder.run {
            if (messages[position].my) {
                textMy.text = messages[position].text
                val sdf = SimpleDateFormat("HH:mm:ss")
                timeMy.text = sdf.format(messages[position].time)
                textMy.visibility = View.VISIBLE
                timeMy.visibility = View.VISIBLE
                textApp.visibility = View.GONE
                timeApp.visibility = View.GONE
            }
            else  {
                textApp.text = messages[position].text
                val sdf = SimpleDateFormat("HH:mm:ss")
                timeApp.text = sdf.format(messages[position].time)
                textApp.visibility = View.VISIBLE
                timeApp.visibility = View.VISIBLE
                textMy.visibility = View.GONE
                timeMy.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    class ViewHolderItemChat(view: View) :
        RecyclerView.ViewHolder(view) {
        val textMy: TextView
        val timeMy: TextView
        val textApp: TextView
        val timeApp: TextView

        init {
            textMy = view.findViewById(R.id.textMessageMy)
            timeMy = view.findViewById(R.id.timeMy)
            textApp = view.findViewById(R.id.textMessageApp)
            timeApp = view.findViewById(R.id.timeApp)
        }

    }


}









