package pro.butovanton.noface.viewmodels

import androidx.lifecycle.ViewModel
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.Repo

class MainViewModel(val repo : Repo) : ViewModel() {

    var user1 = User()
    var userApp = UserApp()

    fun startSearching() {

    }

}