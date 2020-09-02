package pro.butovanton.noface.Models

import android.text.Editable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
open class Massage() {

    var time: Long = 0
    var text: String = ""
    var my = true
    var end = false
    var edit = false

 constructor(time: Long,
    text: String,
    my: Boolean) : this () {

     this.time = time
     this.text = text
     this.my = my

 }

}