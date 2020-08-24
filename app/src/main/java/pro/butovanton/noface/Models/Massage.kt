package pro.butovanton.noface.Models

import android.text.Editable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Massage() {

    var time: Long = 0
    var text: String = ""
    var my: Boolean = true
    var end: Boolean = false

 constructor(time: Long,
    text: String,
    my: Boolean) : this () {

     this.time = time
     this.text = text
     this.my = my

 }

}