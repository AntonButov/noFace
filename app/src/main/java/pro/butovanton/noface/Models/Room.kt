package pro.butovanton.noface.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Room(
    var key: String?,
    var user1: User
) {
    constructor() : this ("", User())
}

