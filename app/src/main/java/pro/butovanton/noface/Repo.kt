package pro.butovanton.noface

import com.google.android.gms.tasks.Task
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import io.reactivex.rxjava3.core.Observable
import pro.butovanton.noface.Models.Room
import javax.inject.Singleton

@Singleton
class Repo(var ref : DatabaseReference) {

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

   fun getRooms() : Observable<Room> {
       return Observable.create { o ->
           var listener = object : ChildEventListener {
               override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                   val room = snapshot.getValue(Room::class.java)
                   o.onNext(room)
               }

               override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

               }

               override fun onChildRemoved(snapshot: DataSnapshot) {
               }

               override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
               }

               override fun onCancelled(error: DatabaseError) {
               }

           }
           ref.addChildEventListener(listener)
       }
   }

}