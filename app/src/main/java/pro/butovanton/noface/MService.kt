package pro.butovanton.noface

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import pro.butovanton.noface.di.App

class MService : Service() {
  //  val repo = (App).appcomponent.getRepo()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d((App).TAG, "Service startComand.")
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d((App).TAG, "Service create.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        //repo.onCancel()
        super.onTaskRemoved(rootIntent)
        Log.d((App).TAG, "Task removed.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d((App).TAG, "Service destroy.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d((App).TAG, "Service bind.")
        return null
    }
}
