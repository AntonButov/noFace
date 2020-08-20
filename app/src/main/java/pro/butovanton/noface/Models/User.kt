package pro.butovanton.noface.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var gender: Int = 0,
    var age: Int = 0)