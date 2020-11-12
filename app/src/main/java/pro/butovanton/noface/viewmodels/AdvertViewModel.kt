package pro.butovanton.noface.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.App


class AdvertViewModel() : ViewModel() {

    private val billing = (App).appcomponent.getBilling()

    fun getIsAdvertDontShow() : Boolean {
        return billing.isAdvertDontShow()
    }

    fun launchBilling(activity: Activity, numSub : Int) {
        if (billing.isValidBilling())
            billing.launchBilling(activity, numSub)
        }
    }
