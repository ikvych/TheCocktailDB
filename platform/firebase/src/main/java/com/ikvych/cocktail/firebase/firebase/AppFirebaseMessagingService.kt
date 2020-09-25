package com.ikvych.cocktail.firebase.firebase

/*import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat*/
import com.ikvych.cocktail.firebase.firebase.model.NotificationModel


/*
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
            if (notificationModel != null) {
                configureNotification(notificationModel)
            }
        }

        // Check if message contains a notification payload.
        p0.notification?.let {
            Log.d("TOKEN_LOGS", "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

    private fun configureNotification(model: NotificationModel) {
        val intent = Intent(this, com.ikvych.cocktail.splash.SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EXTRA_NOTIFICATION, model)
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (model.type == NotificationType.NOTIFICATION_TYPE_COCKTAIL_DETAIL) {
            createCustomNotification(pendingIntent, model)
        } else {
            createRegularNotification(pendingIntent, model)
        }
    }

    private fun createCustomNotification(
        pendingIntent: PendingIntent,
        model: NotificationModel
    ) {
        // сервіс для додавання коктейлю в улюблені
        val serviceIntent = Intent(this, NotificationActionService::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_COCKTAIL_ID, model.cocktailId)
        }
        val servicePendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // налаштовування кастомно вигляду для нотіфікейшина
        val notificationLayout = RemoteViews(packageName, R.layout.layout_drink_detail_push)
        notificationLayout.setTextViewText(R.id.txt_title, model.title)
        notificationLayout.setTextViewText(R.id.txt_description, model.body)
        notificationLayout.setOnClickPendingIntent(
            R.id.ib_is_favorite,
            servicePendingIntent
        )

        val builder = NotificationCompat.Builder(
            this@AppFirebaseMessagingService,
            NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_app_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.DecoratedCustomViewStyle()
            )
            .setCustomContentView(notificationLayout)
        // додавання іконки в нотіфікейшин
        Glide.with(this)
            .asBitmap()
            .load(model.image)
            .transform(CenterCrop(), RoundedCorners(6))
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {*/
/*stub*//*
}
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    notificationLayout.setImageViewBitmap(
                        R.id.iv_drink_image,
                        resource
                    )
                    notifyNotification(builder)
                }
            })
    }

    private fun createRegularNotification(
        pendingIntent: PendingIntent,
        model: NotificationModel
    ) {
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_app_notification)
            .setContentTitle(model.title)
            .setContentText(model.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notifyNotification(builder)
    }

    private fun notifyNotification(builder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(this@AppFirebaseMessagingService)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    companion object {
        const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"
        const val EXTRA_NOTIFICATION_COCKTAIL_ID = "EXTRA_NOTIFICATION_COCKTAIL_ID"
        const val NOTIFICATION_ID = 1995
    }
}*/
