package com.ikvych.cocktail.repository.source

import com.ikvych.cocktail.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.repository.source.base.BaseRepository

interface NotificationRepository : BaseRepository {
    suspend fun saveNotification(model: NotificationRepoModel)
    suspend fun deleteNotification()
    suspend fun getNotification(): NotificationRepoModel?
}