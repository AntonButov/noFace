package pro.butovanton.noface

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.kotlin.subscribeBy
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.App.Companion.TAG
import javax.inject.Singleton

@Singleton
open class Repo(open var ref : DatabaseReference) {

    var myRoom : Room? = null
    lateinit var muser : User
    lateinit var muserApp : UserApp
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
    var cansel = false

    lateinit var listenerRoomsList: ValueEventListener;

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
        muser = user
        muserApp = userApp
        cansel = false
        return Single.create {findRoom ->
            findFreeRoom2()
                .subscribeBy {
                    if (it.equals("guest"))
                            findRoom.onSuccess(it)
                    else
                        if (cansel == false)
                            setNewRoom()
                                  .map { if (deleting == true)  deleteRoom() }
                                  .filter { deleting == false }
                                  .subscribeBy {
                                      if (cansel == false)
                                        subscribeRoom()
                                            .subscribeBy {
                                             setInOut(true)
                                             findRoom.onSuccess("owner")
                                          }
                                      else findRoom.onSuccess("stoping")
                                  }
                       else findRoom.onSuccess("stoping")
            }
        }
    }

    fun findFreeRoom2() : Single<String> {
        return Single.create({  find ->
            getRoomsList()
                .doOnNext { if (it.message1.end || it.message2.end)
                                  deleteRoom(it.key!!)  }
                .filter { myRoom == null &&
                          it.empty &&
                          it.message1.end == false &&
                          it.message2.end == false &&
                          isUserValid(it.user1, it.userApp!!)}
                .take(1)
                .subscribeBy({} , {
                   if (myRoom != null) {
                       setInOut(false)
                       myRefEmpty!!.setValue(false)
                       find.onSuccess("guest")
                   }
                   else find.onSuccess("dontFind")
               } , {
                    setRoom(it)
                    Log.d(TAG, "finded " + it.key) })
             })
    }

    fun getRoomsList() : Observable<Room> {
        return Observable.create ({
            listenerRoomsList = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "listenerRooms")
                    for (data in snapshot.children) {
                        var room = data.getValue(Room::class.java)
                        it.onNext(room)
                    }
                    it.onComplete()
                    ref.removeEventListener(listenerRoomsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    it.onError(Throwable("FireBase not loaded"))
                    ref.removeEventListener(listenerRooms!!)
                }
            }
            ref.addValueEventListener(listenerRoomsList as ValueEventListener)

        }
        )
    }

        fun freeRoomFind(snapshot: DataSnapshot) : Room? {
            var resultRoom : Room? = null
            for (data in snapshot.children) {
                var room = data.getValue(Room::class.java)
                if (room!!.empty && room.message1.end == false && room.message2.end == false &&
                   isUserValid(room.user1, room.userApp!!))     {
                    resultRoom = room
                break
                }
            }
        return resultRoom
        }

    fun isUserValid(user: User, userApp: UserApp) : Boolean {
    return  userApp.gender == muser.gender &&
            muserApp.gender == user.gender &&
            isUserValidAge(user, userApp)
    }

    fun isUserValidAge(user: User, userApp: UserApp) : Boolean {
    var res = true
    if (muser.gender == 2) res = true
    else res = muserApp.age[user.age] && userApp.age[muser.age]
    return res
    }

fun setNewRoom() : Single<Boolean> {
    settingRoom = true
    return Single.create({
        setRoom(Room(getKey(), muser, muserApp))
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


    fun subscribeRoom() : Single<Boolean> {
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
               myRefEmpty!!.setValue(true).addOnSuccessListener {  //открываем комнату
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
        if (myRef != null) {
            myRef!!.removeValue()
            deleting = false
        }
    }

    fun deleteRoom( key : String) {
        ref.child(key).removeValue()
    }

    fun onCancel() {
        cansel = true
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

    fun findFreeRoom() : Single<String> {
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
                            it.onSuccess("dontFind")

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

}