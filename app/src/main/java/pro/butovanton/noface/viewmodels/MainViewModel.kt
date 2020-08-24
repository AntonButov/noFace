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


class MainViewModel(val repo: Repo) : ViewModel() {

    var myRoom : Room? = null

    var user = User()
    var userApp = UserApp()
    lateinit var d : Disposable
    private var getRooms = MutableLiveData<String>(null)

    fun startSearching() : LiveData<String> {

        d = repo.getRooms()
                .filter { it.impty }
              //  .filter { filterByGender(it.user1.gender) }
                .subscribeBy({

                }, {
                    createRoom()
                    getRooms.setValue("")
                },
                    {
                        connectToRoom(it)
                        getRooms.setValue(it.key)
                        d.dispose()
                    })
        return getRooms
    }

    fun stopSearching() {
        d.dispose()

    }

    fun filterByGender(gender: Int) : Boolean {
        if (user.gender == 2) return true
        return userApp.gender == gender
    }

    fun createRoom() {
        myRoom = Room(repo.getKey(), user, userApp)
        repo.saveRoom(myRoom!!)
    }

    fun deleteCreatingRoom() {
        TODO("Удаляем созданую комнату")
    }

    fun connectToRoom(room: Room) {
         room.impty = false
        repo.saveRoom(room)
    }

    fun freeRoom() {

    }
 }