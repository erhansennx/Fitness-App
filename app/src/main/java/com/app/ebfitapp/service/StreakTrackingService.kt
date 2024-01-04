package com.app.ebfitapp.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import java.util.*

class StreakTrackingService : Service() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                Log.d("StreakTrackingService", "Servis RUNNING - ${Date()}")
                handler.postDelayed(this, 15 * 1000)
            }
        }
        handler.post(runnable)
        return START_STICKY
    }

}