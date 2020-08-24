package pro.butovanton.noface.viewmodels

import android.database.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.Repo


class ChatViewModel(val repo: Repo) : ViewModel() {

 var keyRoom = ""

 fun connectToRoom() : io.reactivex.rxjava3.core.Observable<Massage> {
  return repo.connecToChat(keyRoom)
 }

 fun disconnectChat() {
  repo.disConnectFromChat(keyRoom)
 }
 }