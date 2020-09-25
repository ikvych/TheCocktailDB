package com.ikvych.cocktail.repository.impl.mapper.notification

import com.ikvych.cocktail.database.model.notification.NotificationDbModel
import com.ikvych.cocktail.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.repository.model.notification.NotificationRepoModel

class NotificationRepoModelMapper : BaseRepoModelMapper<NotificationRepoModel, NotificationDbModel, Any>() {

    override fun mapDbToRepo(db: NotificationDbModel): NotificationRepoModel = with(db) {
        return NotificationRepoModel(
            id = id,
            type = type,
            image = image,
            cocktailId = cocktailId
        )
    }

    override fun mapRepoToDb(repo: NotificationRepoModel): NotificationDbModel = with(repo) {
        return NotificationDbModel(
            id = id,
            type = type,
            image = image,
            cocktailId = cocktailId
        )
    }
}