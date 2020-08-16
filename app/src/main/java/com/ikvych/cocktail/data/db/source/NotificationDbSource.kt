package com.ikvych.cocktail.data.db.source

import com.ikvych.cocktail.data.db.model.notification.NotificationDbModel
import com.ikvych.cocktail.data.db.source.base.BaseDbSource
import com.ikvych.cocktail.data.repository.model.notification.NotificationRepoModel

interface NotificationDbSource : BaseDbSource {
    suspend fun saveNotification(model: NotificationDbModel)
    suspend fun deleteNotification()
    suspend fun getNotification(): NotificationDbModel?
}