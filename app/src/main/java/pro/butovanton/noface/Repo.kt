package pro.butovanton.noface

import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
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

}