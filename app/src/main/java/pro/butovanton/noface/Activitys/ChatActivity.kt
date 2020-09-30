package pro.butovanton.noface.Activitys

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rockerhieu.emojicon.EmojiconGridFragment
import com.rockerhieu.emojicon.EmojiconsFragment
import com.rockerhieu.emojicon.emoji.Emojicon
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
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
    lateinit var d : Disposable

    private val model: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.toolbar))

       d = model.connectToRoom()
           .subscribeBy({
               Log.d((App).TAG, "оибка connect to Room")
           }, {
               textViewEditmessage.text = "Собеседник покинул чат ..."
               // Нужно прописать выход из чата.

               d.dispose()
               finish()

           },
               {
                   it.my = false
                   if (it.edit)
                       textViewEditmessage.visibility = View.VISIBLE
                   else {
                       if (!it.text.equals("")) {
                           textViewEditmessage.visibility = View.INVISIBLE
                           adapterChat.messages.add(0, it)
                           adapterChat.notifyDataSetChanged()
                           notify(baseContext)
                       }
                   }
               })


        edMessage = findViewById<View>(R.id.editText) as EditText

        edMessage.setOnClickListener {
            it.isFocusable = true
            it.isFocusableInTouchMode = true
        }

        edMessage.addTextChangedListener {
            if (!it.toString().equals("")) {
                var messageEdit = Massage()
                messageEdit.edit = true
                model.sendMessage(messageEdit)
               }
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
            model.sendMessage(message)
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

    fun isSound() : Boolean {
        return PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getBoolean("sound", false)
    }

    fun playSound(context: Context?) {
        val myMediaPlayer: MediaPlayer
        myMediaPlayer = MediaPlayer.create(context, R.raw.notify_sleep_order)
        myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        myMediaPlayer.start()
    }

    fun isVibro() : Boolean {
        return PreferenceManager.getDefaultSharedPreferences(baseContext)
            .getBoolean("vibro", false)
    }

    fun vibrateDevice(context: Context) {
        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= 26) {
                it.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(100)
            }
        }
    }

    fun notify(context: Context) {
        if (isVibro())
            vibrateDevice(context)
        if (isSound())
            playSound(context)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
           menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.action_settings -> {
                val i = Intent()
                i.setClass(this, SettingsActivity::class.java)
                startActivityForResult(i, 0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.disconnectChat()
    }
}


