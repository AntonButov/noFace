package pro.butovanton.noface.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.Job
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App
import pro.butovanton.noface.di.App.Companion.TAG
import pro.butovanton.noface.viewmodels.MainViewModel


class HomeFragment : Fragment(), FindDialogAction {


    val CHAT_ACTIVITY_REQUEST_CODE = 101

    lateinit var mAdView : AdView

    lateinit var bMan1 : Button
    lateinit var bFeMale1 : Button
    lateinit var bAnyGender1 : Button
    lateinit var bMan2 : Button
    lateinit var bFeMale2 : Button
    lateinit var bAnyGender2 : Button

    lateinit var b18_1 : Button
    lateinit var b18_21_1 : Button
    lateinit var b22_25_1 : Button
    lateinit var b26_35_1 : Button
    lateinit var b35_1 : Button
    lateinit var b18_2 : Button
    lateinit var b18_21_2 : Button
    lateinit var b22_25_2 : Button
    lateinit var b26_35_2 : Button
    lateinit var b35_2 : Button

    var disposableDialogCount = CompositeDisposable()
    var disposableSearchigRoom = CompositeDisposable()
    lateinit var textViewCountUsers : TextView

    lateinit var jobCountUsers : Job
   // lateinit var disposableUsersCount : Disposable

    val PEAPLE_COUNT = 2000
    var count = PEAPLE_COUNT

    lateinit var findDialog : FindDialog

    private val model: MainViewModel by viewModels()

    private lateinit var fabChat: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        mAdView = root.findViewById(R.id.adViewHome)
        val adRequest = AdRequest.Builder().build()
        if (!model.getIsAdvertDontShow()) {
            mAdView.loadAd(adRequest)
            mAdView.visibility = VISIBLE
        }
        else
            mAdView.visibility = GONE

        bMan1 = root.findViewById(R.id.man1)
        bFeMale1 = root.findViewById(R.id.feMale1)
        bAnyGender1 = root.findViewById(R.id.anyGender1)

        bMan2 = root.findViewById(R.id.man2)
        bFeMale2 = root.findViewById(R.id.feMale2)
        bAnyGender2 = root.findViewById(R.id.anyGender2)

        b18_1 = root.findViewById(R.id.until18_1)
        b18_21_1 = root.findViewById(R.id.b18_21_1)
        b22_25_1 = root.findViewById(R.id.b21_25_1)
        b26_35_1 = root.findViewById(R.id.b26_35_1)
        b35_1 = root.findViewById(R.id.b35_1)

        b18_2 = root.findViewById(R.id.until18_2)
        b18_21_2 = root.findViewById(R.id.b18_21_2)
        b22_25_2 = root.findViewById(R.id.b21_25_2)
        b26_35_2 = root.findViewById(R.id.b26_35_2)
        b35_2 = root.findViewById(R.id.b35_2)

        bMan1.setOnClickListener {
            model.user.gender = 0
            it.setBackgroundResource(R.drawable.buttons_focus)
            bFeMale1.setBackgroundResource(R.drawable.buttons)
            bAnyGender1.setBackgroundResource(R.drawable.buttons)
            setEnabled(true)
        }
        bFeMale1.setOnClickListener {
            model.user.gender = 1
            bMan1.setBackgroundResource(R.drawable.buttons)
            it.setBackgroundResource(R.drawable.buttons_focus)
            bAnyGender1.setBackgroundResource(R.drawable.buttons)
            setEnabled(true)

        }
        bAnyGender1.setOnClickListener {
            model.user.gender = 2
            bMan1.setBackgroundResource(R.drawable.buttons)
            bFeMale1.setBackgroundResource(R.drawable.buttons)
            it.setBackgroundResource(R.drawable.buttons_focus)

            bAnyGender2.performClick()
            setEnabled(false)
            bMan2.setBackgroundResource(R.drawable.buttons_enabled)
            bFeMale2.setBackgroundResource(R.drawable.buttons_enabled)
            bAnyGender2.setBackgroundResource(R.drawable.buttons_focus)
            b18_1.setBackgroundResource(R.drawable.buttons_enabled)
            b18_21_1.setBackgroundResource(R.drawable.buttons_enabled)
            b22_25_1.setBackgroundResource(R.drawable.buttons_enabled)
            b26_35_1.setBackgroundResource(R.drawable.buttons_enabled)
            b35_1.setBackgroundResource(R.drawable.buttons_enabled)
            b18_2.setBackgroundResource(R.drawable.buttons_enabled)
            b18_21_2.setBackgroundResource(R.drawable.buttons_enabled)
            b22_25_2.setBackgroundResource(R.drawable.buttons_enabled)
            b26_35_2.setBackgroundResource(R.drawable.buttons_enabled)
            b35_2.setBackgroundResource(R.drawable.buttons_enabled)
        }

        bMan2.setOnClickListener {
            model.userApp.gender = 0
            it.setBackgroundResource(R.drawable.buttons_focus)
            bFeMale2.setBackgroundResource(R.drawable.buttons)
            bAnyGender2.setBackgroundResource(R.drawable.buttons)
        }
        bFeMale2.setOnClickListener {
            model.userApp.gender = 1
            bMan2.setBackgroundResource(R.drawable.buttons)
            it.setBackgroundResource(R.drawable.buttons_focus)
            bAnyGender2.setBackgroundResource(R.drawable.buttons)

        }
        bAnyGender2.setOnClickListener {
            model.userApp.gender = 2
            bMan2.setBackgroundResource(R.drawable.buttons)
            bFeMale2.setBackgroundResource(R.drawable.buttons)
            bAnyGender2.setBackgroundResource(R.drawable.buttons_focus)
        }
        performClickGender1()
        performClickGender2()

        b18_1.setOnClickListener {
            model.user.age = 0
            it.setBackgroundResource(R.drawable.buttons_focus)
            b18_21_1.setBackgroundResource(R.drawable.buttons)
            b22_25_1.setBackgroundResource(R.drawable.buttons)
            b26_35_1.setBackgroundResource(R.drawable.buttons)
            b35_1.setBackgroundResource(R.drawable.buttons)
        }

        b18_21_1.setOnClickListener {
            model.user.age = 1
            b18_1.setBackgroundResource(R.drawable.buttons)
            b18_21_1.setBackgroundResource(R.drawable.buttons_focus)
            b22_25_1.setBackgroundResource(R.drawable.buttons)
            b26_35_1.setBackgroundResource(R.drawable.buttons)
            b35_1.setBackgroundResource(R.drawable.buttons)
        }

        b22_25_1.setOnClickListener {
            model.user.age = 2
            b18_1.setBackgroundResource(R.drawable.buttons)
            b18_21_1.setBackgroundResource(R.drawable.buttons)
            b22_25_1.setBackgroundResource(R.drawable.buttons_focus)
            b26_35_1.setBackgroundResource(R.drawable.buttons)
            b35_1.setBackgroundResource(R.drawable.buttons)
        }

        b26_35_1.setOnClickListener {
            model.user.age = 3
            b18_1.setBackgroundResource(R.drawable.buttons)
            b18_21_1.setBackgroundResource(R.drawable.buttons)
            b22_25_1.setBackgroundResource(R.drawable.buttons)
            b26_35_1.setBackgroundResource(R.drawable.buttons_focus)
            b35_1.setBackgroundResource(R.drawable.buttons)
        }

        b35_1.setOnClickListener {
            model.user.age = 4
            b18_1.setBackgroundResource(R.drawable.buttons)
            b18_21_1.setBackgroundResource(R.drawable.buttons)
            b22_25_1.setBackgroundResource(R.drawable.buttons)
            b26_35_1.setBackgroundResource(R.drawable.buttons)
            b35_1.setBackgroundResource(R.drawable.buttons_focus)
        }

        b18_2.setOnClickListener {
            model.userApp.age[0] = !model.userApp.age[0]
            performClickAge2()
        }

        b18_21_2.setOnClickListener {
            model.userApp.age[1] = !model.userApp.age[1]
            performClickAge2()
        }

        b22_25_2.setOnClickListener {
            model.userApp.age[2] = !model.userApp.age[2]
            performClickAge2()
        }

        b26_35_2.setOnClickListener {
            model.userApp.age[3] = !model.userApp.age[3]
            performClickAge2()
        }

        b35_2.setOnClickListener {
            model.userApp.age[4] = !model.userApp.age[4]
            performClickAge2()
        }

        b18_2.performClick()
        performClickAge1()
        performClickAge2()
        bAnyGender1.performClick()


        findDialog = FindDialog.newInstance(Bundle(),this, model.getIsAdvertDontShow())
        fabChat = root.findViewById(R.id.buttonChat) as Button
        fabChat.setOnClickListener {
           // if (mAuth.isAuth()) {
            it.visibility = View.GONE
            if (!findDialog.isResumed)
                findDialog.show((activity as MainActivity).getSupportFragmentManager(), "FindDialog")

          //  }
         //   else {
          //      val toast: Toast =
           //         Toast.makeText(
          //              requireActivity().baseContext,
          //              "Нет подключения к интернету.",
          //              Toast.LENGTH_SHORT
          //          )
         //           toast.show()
          //  }
        }

        textViewCountUsers = root.findViewById<TextView>(R.id.textViewCount)
        jobCountUserStart()

        return root
    }

    fun jobCountUserStart() {
        jobCountUsers = lifecycleScope.launchWhenResumed {
            var users: Long? = 0
                try {
                    users = model.getCountRooms()
                }
                catch (e: Exception) {
                    users = 99
                }
            textViewCountUsers.text = "Число пользователей онлайн: $users"
        }
    }

    fun jobCountUserStop() {
        jobCountUsers.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHAT_ACTIVITY_REQUEST_CODE && !model.getIsAdvertDontShow()) {
            (activity as MainActivity).showInterAdwertChat()
            fabChat.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        fabChat.visibility = VISIBLE
    }

    override fun onPause() {
        super.onPause()
        jobCountUsers.cancel()
      //  disposableUsersCount.dispose()
        if (disposableSearchigRoom.size() > 0 ) {
            onCancel()
        }
        if (findDialog!=null && findDialog.isResumed) {
            findDialog.dismiss()
            fabChat.visibility = VISIBLE
        }
    }

    fun setEnabled(enab: Boolean) {
        bMan2.isEnabled = enab
        bFeMale2.isEnabled = enab
        bAnyGender2.isEnabled = enab
        b18_1.isEnabled = enab
        b18_21_1.isEnabled = enab
        b22_25_1.isEnabled = enab
        b26_35_1.isEnabled = enab
        b35_1.isEnabled = enab
        b18_2.isEnabled = enab
        b18_21_2.isEnabled = enab
        b22_25_2.isEnabled = enab
        b26_35_2.isEnabled = enab
        b35_2.isEnabled = enab

        performClickGender2()
        performClickAge1()
        performClickAge2()
    }

    fun performClickGender1() {
        when (model.user.gender) {
            0 -> bMan1.performClick()
            1 -> bFeMale1.performClick()
            2 -> bAnyGender1.performClick()
        }
    }

    fun performClickGender2() {
        when (model.userApp.gender) {
            0 -> bMan2.performClick()
            1 -> bFeMale2.performClick()
            2 -> bAnyGender2.performClick()
        }
    }

    fun performClickAge1() {
        when (model.user.age) {
            0 -> b18_1.performClick()
            1 -> b18_21_1.performClick()
            2 -> b22_25_1.performClick()
            3 -> b26_35_1.performClick()
        }
    }

    fun performClickAge2() {
        if (model.userApp.age[0])
            b18_2.setBackgroundResource(R.drawable.buttons_focus)
        else
            b18_2.setBackgroundResource(R.drawable.buttons)

        if (model.userApp.age[1])
            b18_21_2.setBackgroundResource(R.drawable.buttons_focus)
        else
            b18_21_2.setBackgroundResource(R.drawable.buttons)

        if (model.userApp.age[2])
            b22_25_2.setBackgroundResource(R.drawable.buttons_focus)
        else
            b22_25_2.setBackgroundResource(R.drawable.buttons)

        if (model.userApp.age[3])
            b26_35_2.setBackgroundResource(R.drawable.buttons_focus)
        else
            b26_35_2.setBackgroundResource(R.drawable.buttons)

        if (model.userApp.age[4])
            b35_2.setBackgroundResource(R.drawable.buttons_focus)
        else
            b35_2.setBackgroundResource(R.drawable.buttons)
    }

    override fun startSearching() {
        jobCountUserStop()
        if (disposableSearchigRoom.size() == 0) {
            Log.d("DEBUG", "Start searching")
            disposableSearchigRoom.add(model.startSearching()
                ?.subscribeBy {
                    if (it.equals("guest") || it.equals("owner")) {
                        var intent = Intent(context, ChatActivity::class.java)
                        startActivityForResult(intent, CHAT_ACTIVITY_REQUEST_CODE)
                    }
                    findDialog.dismiss()
                    disposableSearchigRoom.clear()
                    disposableDialogCount.clear()
                    Log.d(TAG, it);
                })
        }
    }


    override fun onCancel() {
        jobCountUserStart()
        model.onCancel()
        disposableSearchigRoom.clear()
        disposableDialogCount.clear()
        fabChat.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposableSearchigRoom.size() > 0 ) {
            onCancel()
            findDialog.dismiss()
        }
        else model.disconectFromChat()
        disposableDialogCount.dispose()
    }

}

