package com.ikvych.cocktail.presentation.mapper.notification

import com.ikvych.cocktail.data.repository.model.notification.NotificationRepoModel
import com.ikvych.cocktail.data.repository.model.user.UserRepoModel
import com.ikvych.cocktail.presentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.presentation.model.notification.NotificationModel
import com.ikvych.cocktail.presentation.model.notification.NotificationType
import com.ikvych.cocktail.presentation.model.user.UserModel

class NotificationModelMapper : BaseModelMapper<NotificationModel, NotificationRepoModel>() {
    override fun mapTo(model: NotificationRepoModel): NotificationModel = with(model) {
        return NotificationModel (
            id = id,
            type = NotificationType.values().first { it.key == type },
            cocktailId = cocktailId
        )
    }

    override fun mapFrom(model: NotificationModel): NotificationRepoModel = with(model) {
        return NotificationRepoModel(
            id = id,
            type = type.key,
            cocktailId = cocktailId
        )
    }
}