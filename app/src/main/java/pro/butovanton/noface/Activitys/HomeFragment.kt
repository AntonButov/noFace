package pro.butovanton.noface.Activitys

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App
import pro.butovanton.noface.viewmodels.MainViewModel
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

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

    lateinit var progressDialog : ProgressDialog
    var d = CompositeDisposable()
    lateinit var disposable : Disposable
    var count = 2000

    private val model: MainViewModel by viewModels {
        (App).appcomponent.getMainViewModelFactory()
    }

    private var mAuth = (App).appcomponent.getAuth()

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

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

        performClickAge1()

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

        performClickAge2()

        val fabChat = root.findViewById(R.id.fabChat) as FloatingActionButton
        fabChat.setOnClickListener {
            if (mAuth.isAuth()) {
                progressDialog = ProgressDialog(it.context)
                progressDialog.setMessage("Поиск чата")
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                progressDialog.show()
                progressDialog.max = 100
                progressDialog.setOnCancelListener {
                    model.onCancel()
                    d!!.clear()
                }

                d.add(Observable
                    .intervalRange(1, 100, 1, 1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        progressDialog.incrementProgressBy(1)
                    })

                model.startSearching()
                    ?.subscribeBy {
                        var intent = Intent(context, ChatActivity::class.java)
                        startActivityForResult(intent, 101)
                        progressDialog.hide()
                        d.clear()
                    }
            }
            else {
                val toast: Toast =
                    Toast.makeText(requireActivity().baseContext,"Нет подключения к интернету.", Toast.LENGTH_SHORT)
                    toast.show()
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        disposable =  Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            count += (10 -java.util.Random().nextInt(20))
            textViewCount.text = "Число пользователей онлайн: " + count
            }
    }

    override fun onPause() {
        super.onPause()
        disposable.dispose()
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

    override fun onDestroy() {
        super.onDestroy()
        model.onCancel()
        d.dispose()
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

}

