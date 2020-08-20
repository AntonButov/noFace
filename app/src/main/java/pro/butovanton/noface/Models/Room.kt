package pro.butovanton.noface.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Room() {

    var key : String? = null
    lateinit var user1 : User

    constructor(key: String?, user1: User) : this () {
        this.key = key
        this.user1 = user1
    }
}




