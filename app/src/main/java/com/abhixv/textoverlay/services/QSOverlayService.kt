package com.abhixv.textoverlay.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.abhixv.textoverlay.R

class QSOverlayService : Service() {
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification: Notification = createNotification()

        startForeground(1, notification)

        return START_STICKY
    }

    @SuppressLint("ObsoleteSdkInt", "InflateParams")
    override fun onCreate() {
        super.onCreate()

        val density = resources.displayMetrics.density
        val widthInPx = (400 * density).toInt()
        val heightInPx = (600 * density).toInt()

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val params = WindowManager.LayoutParams(
            widthInPx,
            heightInPx,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.x = 0
        params.y = screenHeight - heightInPx

        overlayView = LayoutInflater.from(this).inflate(R.layout.custom_qs_panel, null)
        windowManager!!.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayView?.let {
            windowManager?.removeView(it)
        }
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "qs_overlay_channel"
            val channelName = "QS Overlay Service"
            val channelDescription = "Service for displaying QS Overlay"

            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(this, "qs_overlay_channel")
            .setContentTitle("QS Panel Service")
            .setContentText("Overlay is active")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }
}
