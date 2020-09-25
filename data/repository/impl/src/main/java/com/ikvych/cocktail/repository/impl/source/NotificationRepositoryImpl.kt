package com.ikvych.cocktail.repository.impl.source

import com.ikvych.cocktail.repository.impl.mapper.notification.NotificationRepoModelMapper
import com.ikvych.cocktail.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.database.source.NotificationDbSource
import com.ikvych.cocktail.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.repository.source.NotificationRepository

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