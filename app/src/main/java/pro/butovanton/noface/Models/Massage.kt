package pro.butovanton.noface.Models

import android.text.Editable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Massage(
    var time: Long,
    var text: String )