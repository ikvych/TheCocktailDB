package com.ikvych.cocktail.presentation.model.notification

data class NotificationModel(
    val id: Long = 1L,
    val type: NotificationType = NotificationType.NOTIFICATION_TYPE_UNDEFINED,
    val cocktailId: Long? = -1L
)