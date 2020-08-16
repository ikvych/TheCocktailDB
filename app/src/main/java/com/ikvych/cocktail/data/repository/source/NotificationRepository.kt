package com.ikvych.cocktail.data.repository.source

import com.ikvych.cocktail.data.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.data.repository.source.base.BaseRepository

interface NotificationRepository : BaseRepository {
    suspend fun saveNotification(model: NotificationRepoModel)
    suspend fun deleteNotification()
    suspend fun getNotification(): NotificationRepoModel?
}