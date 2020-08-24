package pro.butovanton.noface

import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Observable
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import javax.inject.Singleton

@Singleton
class Repo(var ref : DatabaseReference) {

    var myRoom : Room? = null
    var owner = false
    var listenerMessage : ValueEventListener? = null

    fun saveRoom( room : Room) : Task<Void> {
        return ref
            .child(room.key.toString())
            .setValue(room)
    }

    fun getKey() : String? {
        return ref
            .push()
            .key
    }

    //val room = snapshot.getValue(Room::class.java)
   // it.onNext(room)

    fun getRooms() : Observable<Room> {
       return Observable.create {
           var listener = object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                     for ( dt in snapshot.children)
                           it.onNext(dt.getValue(Room::class.java))
                     it.onComplete()
               }
               override fun onCancelled(error: DatabaseError) {
                  it.onError(Throwable("FireBase not loaded"))
               }
           }
           ref.addValueEventListener(listener)
       }
   }

    fun connecToChat(idRoom : String) : Observable<Massage> {
        return Observable.create {
            listenerMessage = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                var message1 = snapshot.getValue(Massage::class.java)
                it.onNext(message1)
                if (message1!!.end)
                   it.onComplete()
                }
                override fun onCancelled(error: DatabaseError) {
                    it.onError(Throwable("Message not loaded"))
                }
            }

            ref.child(idRoom).child("message1").addValueEventListener(listenerMessage as ValueEventListener)
        }

    }

    fun disConnectFromChat(idRoom: String) {
        ref.child(idRoom).child("message1").removeEventListener(listenerMessage!!)
    }

    fun createRoom(user : User, userApp : UserApp) {
        myRoom = Room(getKey(), user, userApp)
        saveRoom(myRoom!!)
        owner = true
    }

    fun deleteRoom() {
        TODO("")

        owner = false
    }
}