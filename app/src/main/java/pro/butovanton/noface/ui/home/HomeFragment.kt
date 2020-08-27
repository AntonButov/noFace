package pro.butovanton.noface.ui.home

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import pro.butovanton.noface.Activitys.ChatActivity
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
    lateinit var progressDialog : ProgressDialog

    lateinit var d : Disposable

    private val model: MainViewModel by viewModels {
        (App).appcomponent.getMainViewModelFactory()
    }

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

        bMan1.setOnClickListener {
            model.user.gender = 0
            it.setBackgroundResource(R.color.colorPrimary)
            bFeMale1.setBackgroundResource(R.color.whiteGray)
            bAnyGender1.setBackgroundResource(R.color.whiteGray)
            setEnabled(true)
        }
        bFeMale1.setOnClickListener {
            model.user.gender = 1
            bMan1.setBackgroundResource(R.color.whiteGray)
            it.setBackgroundResource(R.color.colorPrimary)
            bAnyGender1.setBackgroundResource(R.color.whiteGray)
            setEnabled(true)

        }
        bAnyGender1.setOnClickListener {
            model.user.gender = 2
            bMan1.setBackgroundResource(R.color.whiteGray)
            bFeMale1.setBackgroundResource(R.color.whiteGray)
            it.setBackgroundResource(R.color.colorPrimary)

            bAnyGender2.performClick()
            setEnabled(false)
            bMan2.setBackgroundResource(R.color.gray)
            bFeMale2.setBackgroundResource(R.color.gray)
            bAnyGender2.setBackgroundResource(R.color.colorPrimary)
        }

        bMan2.setOnClickListener {
            model.userApp.gender = 0
            it.setBackgroundResource(R.color.colorPrimary)
            bFeMale2.setBackgroundResource(R.color.whiteGray)
            bAnyGender2.setBackgroundResource(R.color.whiteGray)
        }
        bFeMale2.setOnClickListener {
            model.userApp.gender = 1
            bMan2.setBackgroundResource(R.color.whiteGray)
            it.setBackgroundResource(R.color.colorPrimary)
            bAnyGender2.setBackgroundResource(R.color.whiteGray)

        }
        bAnyGender2.setOnClickListener {
            model.userApp.gender = 2
            bMan2.setBackgroundResource(R.color.whiteGray)
            bFeMale2.setBackgroundResource(R.color.whiteGray)
            bAnyGender2.setBackgroundResource(R.color.colorPrimary)
        }
        performClickGender1()
        performClickGender2()

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab.setOnClickListener {
            progressDialog = ProgressDialog(it.context)
            progressDialog.setMessage("Поиск чата")
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog.show()
            progressDialog.max = 100
            progressDialog.setOnCancelListener {
               model.onCancel()
              d.dispose()
            }

           d = Observable
                .intervalRange(1, 100, 1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    progressDialog.incrementProgressBy(1)
                }

            model.startSearching()
                ?.subscribeBy {
                    var intent = Intent(context, ChatActivity::class.java)
                    startActivityForResult(intent, 101)
                    progressDialog.hide()
                    d.dispose()
                }
        }

        return root
    }

    fun setEnabled(enab: Boolean) {
        bMan2.isEnabled = enab
        bFeMale2.isEnabled = enab
        bAnyGender2.isEnabled = enab
        performClickGender2()
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


    override fun onDestroy() {
        super.onDestroy()
        d.dispose()
        model.onCancel()
    }
}

