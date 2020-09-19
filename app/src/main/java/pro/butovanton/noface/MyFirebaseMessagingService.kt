package pro.butovanton.noface

import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pro.butovanton.noface.di.App.Companion.TAG


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var mNotificationManager: NotificationManager? = null
    var notifyId = 0
    var notifyCount = 0

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MyFirebaseMessagingService created")
    }



    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MyFirebaseMessagingService destroed")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived")
        mNotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager // Создаем экземпляр менеджера уведомлений
        val builder: NotificationCompat.Builder
        val CHANNEL_ID = "2"
        val title = if (remoteMessage.notification!!.title != null) remoteMessage.notification!!
            .title else "NoFace"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "NoFace",
                NotificationManager.IMPORTANCE_MIN
            )
            channel.description = "Admin"
            mNotificationManager!!.createNotificationChannel(channel)
        }
        builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setChannelId(CHANNEL_ID)
            .setSmallIcon(R.drawable.chat_bubble_outline_24px)
            .setNumber(notifyCount++)
            .setContentText(remoteMessage.notification!!.body)
            .setContentTitle(title)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
        mNotificationManager!!.notify(notifyId++, builder.build())
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }
}
