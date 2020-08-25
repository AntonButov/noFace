package pro.butovanton.noface

import android.os.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import javax.inject.Singleton

@Singleton
class Repo(var ref : DatabaseReference) {

    var myRoom : Room? = null
    var owner = false
    var listenerRooms : ValueEventListener? = null
    var listenerMessage : ValueEventListener? = null
    var listenerEmpty : ValueEventListener? = null

    fun saveRoom(room: Room?) : Task<Void> {
        return ref
            .child(room!!.key.toString())
            .setValue(room)
    }

    fun getKey() : String? {
        return ref
            .push()
            .key
    }

    fun getRooms(user : User, userApp : UserApp) : Single<String> {
       return Single.create {
           listenerRooms = object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                     for ( dt in snapshot.children) {
                         var room = dt.getValue(Room::class.java)
                         if ( room!!.empty ) {
                             myRoom = room
                             owner = false
                             it.onSuccess("guest")
                             ref.removeEventListener(listenerRooms!!)
                             break
                         }
                     }

               // createRoom(user,userApp)
               }
               override fun onCancelled(error: DatabaseError) {
                  it.onError(Throwable("FireBase not loaded"))
               }
           }
           ref.addValueEventListener(listenerRooms as ValueEventListener)
       }
   }

    fun connecToChat() : Observable<Massage> {
        return Observable.create {
            listenerMessage = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                var message1 = snapshot.getValue(Massage::class.java)
                it.onNext(message1)
                if (message1!!.end) {
                    it.onComplete()
                    disConnectFromChat()
                }
                }
                override fun onCancelled(error: DatabaseError) {
                    it.onError(Throwable("Message not loaded"))
                }
            }
            ref.child(myRoom!!.key.toString()).child("message1").addValueEventListener(listenerMessage as ValueEventListener)
        }
    }

    fun disConnectFromChat() {
       ref.child(myRoom!!.key.toString()).child("message1").removeEventListener(listenerMessage!!)
       myRoom!!.empty = false
       saveRoom(myRoom)
    }

    fun createRoom(user : User, userApp : UserApp) {
        myRoom = Room(getKey(), user, userApp)
        saveRoom(myRoom!!)
        owner = true
    }


    fun sendMessage(message: Massage) {
        ref.child(myRoom!!.key.toString()).child("message2").setValue(message)

    }

    fun deleteRoom() {
        TODO("")

        owner = false
    }
}