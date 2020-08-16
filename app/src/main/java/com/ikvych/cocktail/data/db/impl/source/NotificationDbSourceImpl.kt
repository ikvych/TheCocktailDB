package com.ikvych.cocktail.data.db.impl.source

import com.ikvych.cocktail.data.db.impl.dao.NotificationDao
import com.ikvych.cocktail.data.db.model.notification.NotificationDbModel
import com.ikvych.cocktail.data.db.source.NotificationDbSource

class NotificationDbSourceImpl(
    private val notificationDao: NotificationDao
) : NotificationDbSource{
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