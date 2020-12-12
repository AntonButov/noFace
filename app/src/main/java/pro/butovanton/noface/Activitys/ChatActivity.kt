package pro.butovanton.noface.Activitys

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
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

    val ACTIVITY_SETTING_CODE = 100

    lateinit var edMessage: EditText
    var imogi = false
    var messages = mutableListOf<Massage>()
    private lateinit var recycler: RecyclerView
    private lateinit var adapterChat : RecyclerAdapterChat
    private lateinit var viewManager: RecyclerView.LayoutManager
    lateinit var d : Disposable

    var isSetting = false

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
                       textViewEditmessage.text= " Вводит сообщение"
                   else {
                       if (!it.text.equals("")) {
                           textViewEditmessage.text = ""
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
                startActivityForResult(i, ACTIVITY_SETTING_CODE)
                isSetting = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d((App).TAG, "Chat onResumed")
    }

    override fun onPause() {
        super.onPause()
        Log.d((App).TAG, "Chat onPause")
        //model.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d((App).TAG, "Chat onStop")
      //  if (!isSetting)
             // finish()
    }

    override fun onBackPressed() {
        var firstDialog =  AlertDialog.Builder(this)
            .setTitle("Выход из чата.")
            .setMessage("Вы хотите покинуть чат?")
            .setPositiveButton("Нет.", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
            .setNegativeButton("Да.", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    finish()
                }
            })
            .create()
        firstDialog.show()
    }

    override fun onDestroy() {
        model.disconnectChat()
        Log.d((App).TAG, "Chat onDestroy")
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_SETTING_CODE)
            isSetting = false
    }

}


