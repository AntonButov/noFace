package pro.butovanton.noface.Activitys

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rockerhieu.emojicon.EmojiconGridFragment
import com.rockerhieu.emojicon.EmojiconsFragment
import com.rockerhieu.emojicon.emoji.Emojicon
import kotlinx.android.synthetic.main.activity_chat.*
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App
import pro.butovanton.noface.viewmodels.ChatViewModel
import java.util.*

class ChatActivity : AppCompatActivity(),  EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    lateinit var edMessage: EditText
    var imogi = false
    var messages = mutableListOf<Massage>()
    private lateinit var recycler: RecyclerView
    private lateinit var adapterChat : RecyclerAdapterChat
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val model: ChatViewModel by viewModels {
        (App).appcomponent.getChatViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))

       model.id = intent.getStringExtra("id")

        edMessage = findViewById<View>(R.id.editText) as EditText

        edMessage.setOnClickListener {
            //  Нужно передовать вводит сообщение
        }

        imogiButton.setOnClickListener {
            imogi = !imogi
            if (imogi) {
                imogiButton.setBackgroundResource(R.drawable.keyboard)
                showEmojiPopUp()
            }
            else {
                imogiButton.setBackgroundResource(R.drawable.imogi)
                hideEmojiPopUp()
            }
        }

        sendButton.setOnClickListener {
            var message = Massage(Date().time, edMessage.text.toString(), true)
            adapterChat.messages.add(0, message)
            adapterChat.notifyDataSetChanged()
            edMessage.text.clear()
        }
        setEmojiconFragment(true)

        viewManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        adapterChat = RecyclerAdapterChat(messages)

        recyclerView.apply {
            layoutManager = viewManager
           adapter = adapterChat
        }
}

    fun showKeyboard(editText: EditText?) {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard() {
        val view = findViewById<View>(android.R.id.content)
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_HIDDEN)

        }
    }

    private fun setEmojiconFragment(useSystemDefault: Boolean) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit()
    }

    fun showEmojiPopUp() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var deviceHeight = size.y
        var frameLayout = findViewById<View>(R.id.emojicons) as FrameLayout
        frameLayout.getLayoutParams().height =
            (deviceHeight / 4).toInt() // Setting the height of FrameLayout
        frameLayout.requestLayout()
        frameLayout.setVisibility(View.VISIBLE)
    }

    fun hideEmojiPopUp() {
        val frameLayout = findViewById<View>(R.id.emojicons) as FrameLayout
        frameLayout.visibility = View.GONE
    }


    override fun onEmojiconClicked(emojicon: Emojicon?) {
        EmojiconsFragment.input(edMessage, emojicon);
    }

    override fun onEmojiconBackspaceClicked(v: View?) {
        EmojiconsFragment.backspace(edMessage);
    }

}


