package pro.butovanton.noface.Models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserApp(
    var gender: Int = 2,
    var age: MutableList<Boolean> = mutableListOf(false, false, false,false, false)
)
