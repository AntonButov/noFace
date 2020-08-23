package pro.butovanton.noface.Activitys

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.R


class RecyclerAdapterChat(val messages: MutableList<Massage>) :
    RecyclerView.Adapter<RecyclerAdapterChat.ViewHolderItemChat>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItemChat {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recicler, parent, false)
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

        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    class ViewHolderItemChat(view: View) :
        RecyclerView.ViewHolder(view) {
        val text: TextView

        init {
            text = view.findViewById(R.id.textMessage)

        }

    }


}









