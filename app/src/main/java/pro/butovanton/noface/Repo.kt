package pro.butovanton.noface

import android.os.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.core.operation.Merge
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import javax.inject.Singleton

@Singleton
class Repo(var ref : DatabaseReference) {

    var myRoom : Room? = null
    var myRef : DatabaseReference? = null
    lateinit var myRefEmpty : DatabaseReference
    var refMessageIn : DatabaseReference? = null
    var refMessageOut : DatabaseReference? = null

    var listenerRooms : ValueEventListener? = null
    var listenerMessage : ValueEventListener? = null
    var listenerEmpty : ValueEventListener? = null
    var deleting = false

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

    fun setRoom(room : Room) {
        myRoom = room
        myRef = ref.child(myRoom!!.key.toString())
        myRefEmpty = myRef!!.child("empty")
    }

    fun setInOut(owner : Boolean) {
        if (owner) {
            refMessageIn = myRef?.child("message1")
            refMessageOut = myRef?.child("message2")
        }
        else {
            refMessageIn = myRef!!.child("message2")
            refMessageOut = myRef!!.child("message1")
        }
    }

    fun getRooms(user : User, userApp : UserApp) : Single<String> {
       return Single.create {
           deleting = false
           listenerRooms = object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
               if (myRoom == null) {
                   var room = freeRoomFind(snapshot)
                   if (room != null ) {
                       setRoom(room)
                       setInOut(false)
                       it.onSuccess("guest")
                       myRefEmpty.setValue(false)
                   } else {
                       if (deleting == false) {
                           createRoom(user, userApp)
                               .subscribeBy { itb ->
                                   setInOut(true)
                                   it.onSuccess("owner")
                               }
                       }
                   }
               }
               ref.removeEventListener(listenerRooms!!)
               }
               override fun onCancelled(error: DatabaseError) {
                  it.onError(Throwable("FireBase not loaded"))
               ref.removeEventListener(listenerRooms!!)
               }
           }
           ref.addValueEventListener(listenerRooms as ValueEventListener)
       }
   }

        fun freeRoomFind(snapshot: DataSnapshot) : Room? {
            var resultRoom : Room? = null
            for (data in snapshot.children) {
                var room = data.getValue(Room::class.java)
                if (room!!.empty)
                   resultRoom = room
            break
            }
        return resultRoom
        }

    fun toChat() : Observable<Massage> {
        return Observable.create {
            listenerMessage = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                var messageIn = snapshot.getValue(Massage::class.java)
                if (messageIn != null && messageIn!!.end) {
                    it.onComplete()
                    disConnectFromChat()
                    deleteRoom()
                    return
                }
                if (messageIn != null)
                    it.onNext(messageIn)
                }
                override fun onCancelled(error: DatabaseError) {
                    it.onError(Throwable("Message not loaded"))
                }
            }
             refMessageIn!!.addValueEventListener(listenerMessage as ValueEventListener)
        }
    }

    fun disConnectFromChat() {
        if (myRoom != null) {
            var messagEnd = Massage()
            messagEnd.end = true
            sendMessage(messagEnd)
            refMessageIn!!.removeEventListener(listenerMessage!!)
            myRoom = null
        }
    }

    fun createRoom(user : User, userApp : UserApp) : Single<Boolean> {
        setRoom(Room(getKey(), user, userApp))
        saveRoom(myRoom!!)
        return Single.create {
            listenerEmpty = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var empty = snapshot.getValue(Boolean::class.java)
                        if (empty == false) {
                          it.onSuccess(true)
                          myRefEmpty
                              .removeEventListener(listenerEmpty!!)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    it.onError(Throwable("Message not loaded"))
                }
            }
               myRefEmpty.addValueEventListener(listenerEmpty as ValueEventListener)


        }
    }

    fun deleteRoom() {
        if (myRef != null)
               myRef!!.removeValue()
        myRoom = null
    }

    fun onCancel() {
        if ( refMessageIn != null && listenerEmpty != null) {
            refMessageIn!!.removeEventListener(listenerEmpty!!)
        }

            deleting = true
            deleteRoom()
    }

    fun sendMessage(message: Massage) {
        refMessageOut!!.setValue(message)

    }

}