package com.ikvych.cocktail.data.repository.impl.source

import com.ikvych.cocktail.data.db.model.notification.NotificationDbModel
import com.ikvych.cocktail.data.db.source.NotificationDbSource
import com.ikvych.cocktail.data.repository.impl.mapper.notification.NotificationRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.data.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.data.repository.source.NotificationRepository

class NotificationRepositoryImpl(
    private val notificationDbSource: NotificationDbSource,
    private val mapper: NotificationRepoModelMapper
) : BaseRepositoryImpl(), NotificationRepository {
    override suspend fun saveNotification(model: NotificationRepoModel) {
        notificationDbSource.saveNotification(
            mapper.mapRepoToDb(model)
        )
    }

    override suspend fun deleteNotification() {
        notificationDbSource.deleteNotification()
    }

    override suspend fun getNotification(): NotificationRepoModel? {
        return notificationDbSource.getNotification()?.run(mapper::mapDbToRepo)
    }
}