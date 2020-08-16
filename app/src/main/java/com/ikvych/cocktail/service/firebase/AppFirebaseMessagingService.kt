package com.ikvych.cocktail.service.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ikvych.cocktail.Application.Companion.NOTIFICATION_CHANNEL_ID
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.network.impl.deserializer.BooleanDeserializer
import com.ikvych.cocktail.data.network.impl.deserializer.Iso8601DateDeserializer
import com.ikvych.cocktail.data.network.impl.deserializer.model.CocktailNetModelDeserializer
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetResponse
import com.ikvych.cocktail.presentation.activity.SplashActivity
import com.ikvych.cocktail.presentation.model.notification.NotificationModel
import com.ikvych.cocktail.presentation.model.notification.NotificationType
import com.ikvych.cocktail.service.NotificationActionService
import com.ikvych.cocktail.service.firebase.deserializer.PushModelDeserializer
import io.devlight.data.network.impl.extension.deserializeType
import io.devlight.data.network.impl.extension.toJson
import java.util.*

class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val pushGson: Gson
        get() = GsonBuilder()
            .registerTypeAdapter(
                deserializeType<NotificationModel>(),
                PushModelDeserializer()
            )
            .setPrettyPrinting()
            .serializeNulls()
            .create()

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
            val notificationModel = pushGson.fromJson(
                p0.data.toJson(GsonBuilder().create()),
                NotificationModel::class.java
            )
            // Create an explicit intent for an Activity in your app

            createNotificationChannel(notificationModel)
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

    private fun createNotificationChannel(model: NotificationModel) {

        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_NOTIFICATION, model)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_notification)
            .setContentTitle(model.title)
            .setContentText(model.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (model.type == NotificationType.NOTIFICATION_TYPE_COCKTAIL_DETAIL) {
            val serviceIntent = Intent(this, NotificationActionService::class.java).apply {
                putExtra(EXTRA_NOTIFICATION_COCKTAIL_ID, model.cocktailId)
            }
            val servicePendingIntent: PendingIntent =
                PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            Glide.with(this)
                .asBitmap()
                .load(model.image)
                .into(object : CustomTarget<Bitmap>() {

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        builder
                            .setLargeIcon(resource)
                            .setStyle(
                                NotificationCompat.BigPictureStyle()
                                    .bigPicture(resource)
                                    .bigLargeIcon(null)
                            )
                            .addAction(R.drawable.ic_checbox_favorite_enabled, "Favorite", servicePendingIntent)

                        with(NotificationManagerCompat.from(this@AppFirebaseMessagingService)) {
                            // notificationId is a unique int for each notification that you must define
                            notify(NOTIFICATION_ID, builder.build())
                        }
                    }

                })
        } else {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    companion object {
        const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"
        const val EXTRA_NOTIFICATION_COCKTAIL_ID = "EXTRA_NOTIFICATION_COCKTAIL_ID"
        const val NOTIFICATION_ID = 1995
    }
}