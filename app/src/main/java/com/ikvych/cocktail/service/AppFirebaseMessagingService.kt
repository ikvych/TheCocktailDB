package com.ikvych.cocktail.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("TOKEN_LOGS", p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TOKEN_LOGS", "From: ${p0.from}")

        // Check if message contains a data payload.
        if (p0.data.isNotEmpty()) {
            Log.d("TOKEN_LOGS", "Message data payload: ${p0.data}")

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
/*                scheduleJob()*/
            } else {
                // Handle message within 10 seconds
/*                handleNow()*/
            }
        }

        // Check if message contains a notification payload.
        p0.notification?.let {
            Log.d("TOKEN_LOGS", "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}