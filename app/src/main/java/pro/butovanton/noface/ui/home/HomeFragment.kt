package pro.butovanton.noface.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_home.*
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App
import pro.butovanton.noface.viewmodels.MainViewModel

class HomeFragment : Fragment() {

    private val model: MainViewModel by viewModels {
        (App).appcomponent.getMainViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val bMan1 : Button = root.findViewById(R.id.man1)
        val bFeMale1 : Button = root.findViewById(R.id.feMale1)
        val bAnyGender1 : Button = root.findViewById(R.id.anyGender1)
        bMan1.setOnClickListener {
            model.user1.age = 0
            it.setBackgroundResource(R.color.colorPrimary)
            bFeMale1.setBackgroundResource(R.color.whiteGray)
            bAnyGender1.setBackgroundResource(R.color.whiteGray)
        }
        bFeMale1.setOnClickListener {
            model.user1.age = 1
            bMan1.setBackgroundResource(R.color.whiteGray)
            it.setBackgroundResource(R.color.colorPrimary)
            bAnyGender1.setBackgroundResource(R.color.whiteGray)

        }
        bAnyGender1.setOnClickListener {
            model.user1.age = 2
            bMan1.setBackgroundResource(R.color.whiteGray)
            bFeMale1.setBackgroundResource(R.color.whiteGray)
            it.setBackgroundResource(R.color.colorPrimary)
        }
      return root
    }
}

