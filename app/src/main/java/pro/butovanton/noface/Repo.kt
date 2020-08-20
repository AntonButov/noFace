package pro.butovanton.noface

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import pro.butovanton.noface.Models.Room

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

   fun getRooms() {
    var room = Room()
   }

}