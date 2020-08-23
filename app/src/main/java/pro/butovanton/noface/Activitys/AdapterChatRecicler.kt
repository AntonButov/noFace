package pro.butovanton.noface.Activitys

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rockerhieu.emojicon.EmojiconTextView
import pro.butovanton.noface.R

class AdapterChatRecicler(private val myDataset: Array<String>) :
    RecyclerView.Adapter<AdapterChatRecicler.MyViewHolder>() {

    class MyViewHolder(val textView: EmojiconTextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AdapterChatRecicler.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recicler, parent, false) as EmojiconTextView

        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}