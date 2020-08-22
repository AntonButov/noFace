package pro.butovanton.noface.viewmodels

import androidx.lifecycle.ViewModel
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.Repo

class MainViewModel(val repo : Repo) : ViewModel() {

    var user = User()
    var userApp = UserApp()

    fun startSearching() {

        repo.getRooms()
            .filter { filterByGender(it.user1.gender) }
            .subscribe {

            }


    }

    fun filterByGender(gender : Int) : Boolean {
        if (user.gender == 2) return true
        return userApp.gender == gender
    }
 }