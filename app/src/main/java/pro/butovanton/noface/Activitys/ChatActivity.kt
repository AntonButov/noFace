package pro.butovanton.noface.Activitys

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.rockerhieu.emojicon.EmojiconEditText
import com.rockerhieu.emojicon.EmojiconGridFragment
import com.rockerhieu.emojicon.EmojiconsFragment
import com.rockerhieu.emojicon.emoji.Emojicon
import kotlinx.android.synthetic.main.activity_chat.*
import pro.butovanton.noface.R


class ChatActivity : AppCompatActivity(),  EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    lateinit var edMessage: EmojiconEditText
    var imogi = false
    var messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))
        // On clicking the edit text Emoji panel will be hidden
        edMessage = findViewById<View>(R.id.edtMessage) as EmojiconEditText

        edMessage.setOnClickListener {
          //  Нужно передовать вводит сообщение
        }

        imogiButton.setOnClickListener {
            if (imogi) {
                imogiButton.setBackgroundResource(R.drawable.keyboard)
//                hideKeyboard()
                showEmojiPopUp()
            }
            else {
                imogiButton.setBackgroundResource(R.drawable.imogi)
                hideEmojiPopUp()
         //       showKeyboard(edMessage)
            }
        imogi = !imogi
        }

        sendButton.setOnClickListener {
            edit.text = edMessage.text
        }
        setEmojiconFragment(true)
      //  hideKeyboard()
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
        //    Log.e("Device Height", String.valueOf(deviceHeight))
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



