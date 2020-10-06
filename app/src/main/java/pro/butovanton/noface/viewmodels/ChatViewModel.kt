package pro.butovanton.noface.viewmodels

import androidx.lifecycle.ViewModel
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.di.App


class ChatViewModel() : ViewModel() {

 val repo = (App).appcomponent.getRepo()

 fun connectToRoom() : io.reactivex.rxjava3.core.Observable<Massage> {
  return repo.toChat()
 }

 fun disconnectChat() {
  repo.disConnectFromChat()
 }

 fun sendMessage(message : Massage) {
  repo.sendMessage(message)
 }

 fun onPause() {
  repo.onPause()
 }

 }