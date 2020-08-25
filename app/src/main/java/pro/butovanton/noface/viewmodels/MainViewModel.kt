package pro.butovanton.noface.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
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
    lateinit var d : Disposable
    private var getRooms = MutableLiveData<String>(null)

    fun startSearching() : LiveData<String> {

         repo.getRooms(user, userApp)
                .subscribeBy {
                getRooms.value = it
                }
     return getRooms
    }


 }