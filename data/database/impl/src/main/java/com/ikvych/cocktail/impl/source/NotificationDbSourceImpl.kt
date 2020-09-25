package com.ikvych.cocktail.impl.source

import com.ikvych.cocktail.database.model.notification.NotificationDbModel
import com.ikvych.cocktail.database.source.NotificationDbSource
import com.ikvych.cocktail.impl.dao.NotificationDao


class NotificationDbSourceImpl(
    private val notificationDao: NotificationDao
) : NotificationDbSource {
    override suspend fun saveNotification(model: NotificationDbModel) {
        notificationDao.saveNotification(model)
    }

    override suspend fun deleteNotification() {
        notificationDao.deleteNotification()
    }

    override suspend fun getNotification(): NotificationDbModel? {
        return notificationDao.getNotification()
    }
}