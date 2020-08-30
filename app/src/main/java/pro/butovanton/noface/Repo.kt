package pro.butovanton.noface

import android.os.Message
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.core.operation.Merge
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.kotlin.subscribeBy
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.App.Companion.TAG
import javax.inject.Singleton

@Singleton
class Repo(var ref : DatabaseReference) {

    var myRoom : Room? = null
    var myRef : DatabaseReference? = null
    var myRefEmpty :  DatabaseReference? = null
    var refMessageIn : DatabaseReference? = null
    var refMessageOut : DatabaseReference? = null

    var listenerRooms : ValueEventListener? = null
    var listenerMessage : ValueEventListener? = null
    var listenerEmpty : ValueEventListener? = null
    var deleting = false
    var listenerRoomsDispose = false
    var settingRoom = false

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
        return Single.create {find ->
            findFreeRoom(user, userApp)
                .subscribeBy {
                    if (it.equals("guest"))
                       find.onSuccess("guest")
                    else
                        if (settingRoom == false)
                            setRoom(user, userApp)
                                  .map { if (deleting == true)  deleteRoom() }
                                  .filter { deleting == false }
                                   .subscribeBy {
                                        createRoom(user, userApp)
                                            .subscribeBy { itb ->
                                                setInOut(true)
                                             find.onSuccess("owner")
                                          }
                                  }
                    Log.d(TAG,it)
            }
        }
    }

    fun findFreeRoom(user: User, userApp: UserApp) : Single<String> {
        return Single.create({
            listenerRooms = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "listenerRooms")
                deleteDesertedRoom(snapshot)
                if (myRoom == null && listenerRoomsDispose == false) {
                    var room = freeRoomFind(snapshot)
                    if (room != null ) {
                        setRoom(room)
                        setInOut(false)
                        it.onSuccess("guest")
                        myRefEmpty!!.setValue(false)
                        listenerRoomsDispose = true
                    } else {
                        it.onSuccess("owner")

                    }
                }
                ref.removeEventListener(listenerRooms!!)
                listenerRoomsDispose = true
            }
            override fun onCancelled(error: DatabaseError) {
                it.onError(Throwable("FireBase not loaded"))
                ref.removeEventListener(listenerRooms!!)
            }
        }
    ref.addValueEventListener(listenerRooms as ValueEventListener)
    listenerRoomsDispose = false
    }
        )
    }

        fun freeRoomFind(snapshot: DataSnapshot) : Room? {
            var resultRoom : Room? = null
            for (data in snapshot.children) {
                var room = data.getValue(Room::class.java)
                if (room!!.empty && room.message1.end == false && room.message2.end == false) {
                    resultRoom = room
                break
                }
            }
        return resultRoom
        }

fun setRoom(user: User, userApp: UserApp) : Single<Boolean> {
    settingRoom = true
    return Single.create({
        setRoom(Room(getKey(), user, userApp))
        Log.d(TAG, "Room: " + myRoom!!.key.toString())
        saveRoom(myRoom!!)
            .addOnSuccessListener {  sucses ->
                if (it != null) {
                    it.onSuccess(true)
                    settingRoom = false
                }
            }
    })
}

    fun toChat() : Observable<Massage> {
        return Observable.create {
            listenerMessage = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "listenerMessage")
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
                    Log.d(TAG, "To chat error")
                    it.onError(Throwable("Message not loaded"))
                }
            }
             refMessageIn!!.addValueEventListener(listenerMessage as ValueEventListener)
        }
    }


    fun createRoom(user : User, userApp : UserApp) : Single<Boolean> {
        return Single.create {
            listenerEmpty = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "listenerEmpty")
                    var empty = snapshot.getValue(Boolean::class.java)
                        if (empty == false) {
                          it.onSuccess(true)
                            myRefEmpty
                                ?.removeEventListener(listenerEmpty!!)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                //    it.onError(Throwable("Message not loaded"))
                    Log.d(TAG, "onCanceled firebase")
                }
            }
               myRefEmpty!!.setValue(true).addOnSuccessListener {//открываем комнату
                   myRefEmpty!!.addValueEventListener(listenerEmpty as ValueEventListener)
               }

        }
    }

    fun disConnectFromChat() {
        if (myRoom != null) {
            var messagEnd = Massage()
            messagEnd.end = true
            sendMessage(messagEnd)
            refMessageIn!!.removeEventListener(listenerMessage!!)
            myRoom = null
            Log.d(TAG, "Room: null")
        }
    }

    fun deleteDesertedRoom( dataSnapshot: DataSnapshot) {
        for (data in dataSnapshot.children) {
            var room = data.getValue(Room::class.java)
                if (room!!.message1.end || room.message2.end)
                        deleteRoom(data.key!!)
        }
    }

    fun deleteRoom() {
        myRoom = null
        Log.d(TAG, "Room: null")
        if (myRefEmpty != null && listenerEmpty != null)
             myRefEmpty!!
                      .removeEventListener(listenerEmpty!!)
        if (myRef != null)
               myRef!!.removeValue().addOnSuccessListener {
                   deleting = false
               }
    }

    fun deleteRoom( key : String) {
        ref.child(key).removeValue()
    }

    fun onCancel() {
        if ( refMessageIn != null && listenerEmpty != null) {
            refMessageIn!!.removeEventListener(listenerEmpty!!)
        }
            if (settingRoom) deleting = true
            else
              deleteRoom()
    }

    fun sendMessage(message: Massage) {
        refMessageOut!!.setValue(message)

    }

}