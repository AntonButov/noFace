package pro.butovanton.noface.Activitys

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.rockerhieu.emojicon.EmojiconEditText
import com.rockerhieu.emojicon.EmojiconGridFragment
import com.rockerhieu.emojicon.EmojiconsFragment
import com.rockerhieu.emojicon.emoji.Emojicon
import pro.butovanton.noface.R


class ChatActivity : AppCompatActivity(),  EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    lateinit var edMessage: EmojiconEditText
    // On clicking the edit text Emoji panel will be hidden

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))
        // On clicking the edit text Emoji panel will be hidden
        edMessage = findViewById<View>(R.id.edtMessage) as EmojiconEditText

        edMessage.setOnClickListener {
            var hideEmoji = true
            hideEmojiPopUp(hideEmoji)
            showKeyboard(edMessage)
        }

        setEmojiconFragment(true)
        showEmojiPopUp(true)

    }

    fun showKeyboard(editText: EditText?) {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        hideEmojiPopUp(true)
        //setHeightOfEmojiEditText();
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun setEmojiconFragment(useSystemDefault: Boolean) {
        // Replacing the existing frame having id emojicons with the fragment of emoticons library containing emoticons
        supportFragmentManager.beginTransaction()
            .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit()
    }

    fun showEmojiPopUp(showEmoji: Boolean) {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var deviceHeight = size.y
        //    Log.e("Device Height", String.valueOf(deviceHeight))
        var frameLayout = findViewById<View>(R.id.emojicons) as FrameLayout
        frameLayout.getLayoutParams().height =
            (deviceHeight / 2.5).toInt() // Setting the height of FrameLayout
        frameLayout.requestLayout()
        frameLayout.setVisibility(View.VISIBLE)
       // hideKeyboard()
    }

    fun hideEmojiPopUp(hideEmoji: Boolean) {
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


