package pro.butovanton.noface.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.App


class MainViewModel() : ViewModel() {

    val repo = (App).appcomponent.getRepo()
    val billing = (App).appcomponent.getBilling()
    var searching : Single<String>? = null

    var user = User()
    var userApp = UserApp()

    fun startSearching() : Single<String>? {
        repo.cansel = false
        if (searching == null)
          searching = repo.getRooms(user, userApp)
              .doOnSuccess {
                searching == null
               }

        return searching
    }

    fun onCancel() {
        repo.onCancel()
    }

    fun disconectFromChat() {
        repo.disConnectFromChat()
    }

    fun getIsAdvertDontShow() : Boolean {
        return billing.isAdvertDontShow()
    }

    suspend fun getCountRooms() : Long? {
        return repo.getRoomsCount()
    }

 }