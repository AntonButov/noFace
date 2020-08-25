package pro.butovanton.noface.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Room() {

             var key : String? = null
             var empty = true
    lateinit var user1 : User
    var user2  = User()
             var userApp : UserApp? = null
    var message1 = Massage(0, "", true)
    var message2 = Massage(0, "", false)

    constructor(key: String?, user1: User, userApp : UserApp) : this () {
        this.key = key
        this.user1 = user1
        this.userApp = userApp
    }
}




