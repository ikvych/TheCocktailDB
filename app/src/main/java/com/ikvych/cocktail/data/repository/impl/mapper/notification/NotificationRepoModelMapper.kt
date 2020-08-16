package com.ikvych.cocktail.data.repository.impl.mapper.notification

import com.ikvych.cocktail.data.db.model.notification.NotificationDbModel
import com.ikvych.cocktail.data.db.model.user.entity.UserDbModel
import com.ikvych.cocktail.data.network.model.UserNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.data.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.data.repository.model.user.UserRepoModel

class NotificationRepoModelMapper : BaseRepoModelMapper<NotificationRepoModel, NotificationDbModel, Any>() {

    override fun mapDbToRepo(db: NotificationDbModel): NotificationRepoModel = with(db) {
        return NotificationRepoModel(
            id = id,
            type = type,
            cocktailId = cocktailId
        )
    }

    override fun mapRepoToDb(repo: NotificationRepoModel): NotificationDbModel = with(repo) {
        return NotificationDbModel(
            id = id,
            type = type,
            cocktailId = cocktailId
        )
    }
}