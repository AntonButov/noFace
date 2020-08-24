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
           if (messages[itemCount -1].my)
               view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recicler_my,
            parent,
            false
        )
            else view = LayoutInflater.from(parent.context).inflate(
               R.layout.item_recicler_app,
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
            text.text = messages[position].text
            val sdf = SimpleDateFormat("HH:mm:ss")
            time.text = sdf.format(messages[position].time)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    class ViewHolderItemChat(view: View) :
        RecyclerView.ViewHolder(view) {
        val text: TextView
        val time: TextView

        init {
            text = view.findViewById(R.id.textMessage)
            time = view.findViewById(R.id.time)
        }

    }


}









