package com.abhixv.textoverlay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.abhixv.textoverlay.services.QSOverlayService


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Settings.canDrawOverlays(this)) {
            val intent = Intent(this, QSOverlayService::class.java)
            startService(intent)
        } else {
            // Request permission if not granted
            val overlayPermissionIntent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(overlayPermissionIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, QSOverlayService::class.java)
        stopService(intent)
    }
}