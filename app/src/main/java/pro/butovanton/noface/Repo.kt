package pro.butovanton.noface

import android.os.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
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
    lateinit var myRef : DatabaseReference
    lateinit var refMessageIn : DatabaseReference
    lateinit var refMessageOut : DatabaseReference
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

    fun setRoom(room : Room) {
        myRoom = room
        myRef = ref.child(myRoom!!.key.toString())
    }

    fun setInOut(owner : Boolean) {
        if (owner) {
            refMessageIn = myRef.child("message1")
            refMessageOut = myRef.child("message2")
        }
        else {
            refMessageIn = myRef.child("message2")
            refMessageOut = myRef.child("message1")
        }
    }

    fun getRooms(user : User, userApp : UserApp) : Single<String> {
       return Single.create {
           listenerRooms = object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                     for ( dt in snapshot.children) {
                         var room = dt.getValue(Room::class.java)
                         if ( room!!.empty ) {
                             setRoom(room)
                             owner = false
                             setInOut(owner)
                             it.onSuccess("guest")
                             myRef.child("empty").setValue(false)
                             ref.removeEventListener(listenerRooms!!)
                             return
                         }
                     }
               createRoom(user,userApp)
                   .subscribeBy {itb ->
                          owner = true
                          setInOut(owner)
                          it.onSuccess("owner")
                   }
               ref.removeEventListener(listenerRooms!!)
               }
               override fun onCancelled(error: DatabaseError) {
                  it.onError(Throwable("FireBase not loaded"))
               }
           }
           ref.addValueEventListener(listenerRooms as ValueEventListener)
       }
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
             refMessageIn.addValueEventListener(listenerMessage as ValueEventListener)
        }
    }

    fun disConnectFromChat() {
        if (myRoom != null) {
            var messagEnd = Massage()
            messagEnd.end = true
            sendMessage(messagEnd)
            refMessageIn.removeEventListener(listenerMessage!!)

        }
    }

    fun createRoom(user : User, userApp : UserApp) : Single<Boolean> {
        setRoom(Room(getKey(), user, userApp))
        saveRoom(myRoom!!)
        owner = true
        return Single.create {
            listenerEmpty = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var empty = snapshot.getValue(Boolean::class.java)
                    if (empty == false) {
                        it.onSuccess(true)
                        myRef.child("empty")
                            .removeEventListener(listenerEmpty!!)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    it.onError(Throwable("Message not loaded"))
                }
            }
            myRef.child("empty").addValueEventListener(listenerEmpty as ValueEventListener)


        }
    }

    fun onCancel() {
        if ( refMessageIn != null) {
            refMessageIn.removeEventListener(listenerEmpty!!)
            deleteRoom()
        }
    }

    fun sendMessage(message: Massage) {
        refMessageOut.setValue(message)

    }

    fun deleteRoom() {
        myRef.removeValue()
        myRoom = null
    }
}