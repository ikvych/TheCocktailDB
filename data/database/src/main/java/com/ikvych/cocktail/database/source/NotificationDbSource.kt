package com.ikvych.cocktail.database.source

import com.ikvych.cocktail.database.model.notification.NotificationDbModel
import com.ikvych.cocktail.database.source.base.BaseDbSource


interface NotificationDbSource : BaseDbSource {
    suspend fun saveNotification(model: NotificationDbModel)
    suspend fun deleteNotification()
    suspend fun getNotification(): NotificationDbModel?
}