package pro.butovanton.noface

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import pro.butovanton.noface.Models.Massage
import pro.butovanton.noface.Models.Room
import pro.butovanton.noface.Models.User
import pro.butovanton.noface.Models.UserApp
import pro.butovanton.noface.di.App.Companion.TAG
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//@Singleton
open class Repo(open var ref : DatabaseReference) {

    private var myRoom : Room? = null
    private lateinit var muser : User
    private lateinit var muserApp : UserApp
    private var myRef : DatabaseReference? = null
    private var myRefEmpty :  DatabaseReference? = null
    private var refMessageIn : DatabaseReference? = null
    private var refMessageOut : DatabaseReference? = null

    private var listenerRooms : ValueEventListener? = null
    private var listenerMessage : ValueEventListener? = null
    private var listenerEmpty : ValueEventListener? = null
    private var deleting = false
    private var listenerRoomsDispose = false
    private var settingRoom = false
    var cansel = false
    private var finding = false
    private var subscribingRoom = false;

    private var disposeSubscribeRoom : Disposable? = null

    private lateinit var listenerRoomsList: ValueEventListener;

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
        return Single.create {findRoom ->
            if (finding == false)
            findFreeRoom2()
                .subscribeBy {
                    if (it.equals("guest"))
                            findRoom.onSuccess(it)
                    else
                        if (cansel) {findRoom.onSuccess("Stoping")
                            cansel = false }
                        else
                            setNewRoom()
                                  .map { if (deleting == true)  deleteRoom() }
                                  .filter { deleting == false }
                                  .subscribeBy {
                                      if (cansel) {findRoom.onSuccess("Stoping")
                                          cansel = false }
                                      else
                                      disposeSubscribeRoom = subscribeRoom()
                                                  .subscribeBy {
                                                       setInOut(true)
                                                       findRoom.onSuccess("owner")
                                                  }
                                  }
              }
        }
    }

    private fun findFreeRoom2() : Single<String> {
        Log.d(TAG, "Finding")
        finding = true
        return Single.create({  find ->
            getRoomsList()
                .doOnNext {
                    if (it.message1.end || it.message2.end || it.empty == null)
                                  deleteRoom(it.key!!)
                }
                .filter { myRoom == null && it.empty != null &&
                        it?.empty!! &&
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
                   finding = false
               } , {
                    setRoom(it)
                    Log.d(TAG, "finded " + it.key) })
             })
    }

    private fun getRoomsList() : Observable<Room> {
        return Observable.create ({
            listenerRoomsList = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "listenerRooms")
                    for (data in snapshot.children) {
                        var room = data.getValue(Room::class.java)
                        room?.key = data.key
                        it.onNext(room)
                    }
                    it.onComplete()
                }

                override fun onCancelled(error: DatabaseError) {
                    it.onError(Throwable("FireBase not loaded"))
                }
            }
            ref.addListenerForSingleValueEvent(listenerRoomsList)

        }
        )
    }


    
    suspend fun getRoomsCount() : Long? = suspendCoroutine {continuation ->
        listenerRoomsList = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "listenerRoomsForCount = " + snapshot.childrenCount)
                continuation.resume(snapshot.childrenCount)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "listenerRoomsForCountError = " + error.message)
                continuation.resume(99)
            }
        }
        ref.addListenerForSingleValueEvent(listenerRoomsList)

    }
 
    private fun isUserValid(user: User, userApp: UserApp) : Boolean {
    return  isUserValidGender(user, userApp) &&
            isUserValidAge(user, userApp)
    }

    private fun isUserValidGender(user: User, userApp: UserApp) : Boolean{
        return userApp.gender == muser.gender &&
                muserApp.gender == user.gender
    }

    private fun isUserValidAge(user: User, userApp: UserApp) : Boolean {
    var res : Boolean
    if (muser.gender == 2) res = true
    else res = muserApp.age[user.age] && userApp.age[muser.age]
    return res
    }

    private fun setNewRoom() : Single<Boolean> {
    Log.d(TAG, "SetNewRoom")
    settingRoom = true
    return Single.create({settingRoomEmit ->
        setRoom(Room(getKey(), muser, muserApp))
        Log.d(TAG, "Room: " + myRoom!!.key.toString())
        saveRoom(myRoom!!)
            .addOnSuccessListener {
                if (settingRoomEmit != null) {
                    settingRoomEmit.onSuccess(true)
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
                    refMessageIn!!.removeEventListener(listenerMessage!!)
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


    private fun subscribeRoom() : Single<Boolean> {
        subscribingRoom = true;
        Log.d(TAG, "SubscribeRoom")
        return Single.create {
            listenerEmpty = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "listenerEmpty")
                    var empty = snapshot.getValue(Boolean::class.java)
                        if (empty == false) {
                          it.onSuccess(true)
                            myRefEmpty
                                ?.removeEventListener(listenerEmpty!!)
                             subscribingRoom = false
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
            Log.d(TAG, "Send end message")
            myRoom = null
            Log.d(TAG, "Disconect from chat,")
        }
    }

    private fun deleteRoom() {
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

    private fun deleteRoom( key : String) {
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
}