package com.ikvych.cocktail

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.amitshekhar.DebugDB
import com.ikvych.cocktail.di.Injector
import java.util.*

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
        registerNotificationChanel()
    }

    private fun registerNotificationChanel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.push_notification_chanel)
            val descriptionText = getString(R.string.push_notification_chanel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
    }

}