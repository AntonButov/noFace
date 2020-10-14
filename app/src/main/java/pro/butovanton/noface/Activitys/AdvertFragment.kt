package pro.butovanton.noface.Activitys

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchasesResult
import kotlinx.android.synthetic.main.fragment_advert.*
import pro.butovanton.noface.R
import pro.butovanton.noface.di.App
import pro.butovanton.noface.di.App.Companion.TAG
import pro.butovanton.noface.viewmodels.AdvertViewModel
import pro.butovanton.noface.viewmodels.MainViewModel
import java.util.*


class AdvertFragment : Fragment() {

    private val model: AdvertViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_advert, container, false)

        val adverMountButton = root.findViewById<Button>(R.id.adverMounth)
        adverMountButton.setOnClickListener {
             model.launchBilling(requireActivity())
        }

        if (model.getIsAdvertDontShow())
            adverMountButton.visibility = View.INVISIBLE
        else
            adverMountButton.visibility = View.VISIBLE
        return root
    }

}