package com.app.ebfitapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.app.ebfitapp.R
import com.app.ebfitapp.utils.StreakWidget
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.Date

class StreakTrackingService : Service() {

    private val streaksString = "streaks"
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var firebaseFirestoreService: FirebaseFirestoreService

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channelID = "Foreground Service ID"
        val channel = NotificationChannel(channelID, channelID, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification = Notification.Builder(this, channelID)
            .setContentText("Streak service is running")
            .setContentTitle("Streak service is unable")
            .setSmallIcon(R.drawable.app_logo)
            .build()

        startForeground(1001, notification)

        Thread {
            try {
                firebaseAuthService = FirebaseAuthService(applicationContext)
                firebaseFirestoreService = FirebaseFirestoreService(applicationContext)

                val documentReference =
                    firebaseFirestoreService.firestore.collection(streaksString)
                        .document(firebaseAuthService.getCurrentUserEmail())

                documentReference.addSnapshotListener { snapshot: DocumentSnapshot?, error: FirebaseFirestoreException? ->
                    if (error != null) {
                        println(error)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val firestoreNumber = snapshot.getLong("count")?.toInt() ?: 0
                        val streakDate = snapshot.getString("date")
                        updateWidget(firestoreNumber, streakDate!!)
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }.start()

        stopForeground(true)

        return START_STICKY
    }

    private fun updateWidget(firestoreNumber: Int, date: String) {
        Handler(Looper.getMainLooper()).post {
            val view = RemoteViews(packageName, R.layout.streak_widget)
            view.setTextViewText(R.id.streakWidget, firestoreNumber.toString())
            view.setTextViewText(R.id.streakDateWidget, date)

            val theWidget = ComponentName(this, StreakWidget::class.java)
            val manager = AppWidgetManager.getInstance(this)
            manager.updateAppWidget(theWidget, view)
        }

    }
}