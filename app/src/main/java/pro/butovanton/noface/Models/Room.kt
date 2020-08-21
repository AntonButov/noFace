package pro.butovanton.noface.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Room() {

             var key : String? = null
    lateinit var user1 : User
             var userApp : UserApp? = null

    constructor(key: String?, user1: User, user2 : UserApp) : this () {
        this.key = key
        this.user1 = user1
        this.userApp = user2
    }
}




