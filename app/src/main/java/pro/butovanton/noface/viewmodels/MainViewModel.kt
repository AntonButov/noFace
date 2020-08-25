package pro.butovanton.noface.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.subscribeBy
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.Repo
import pro.butovanton.noface.di.App


class MainViewModel() : ViewModel() {

    val repo = (App).appcomponent.getRepo()

    var user = User()
    var userApp = UserApp()

    fun startSearching() : Single<String> {
        return repo.getRooms(user, userApp)
    }

    fun onCancel() {
        repo.onCancel()
    }

 }