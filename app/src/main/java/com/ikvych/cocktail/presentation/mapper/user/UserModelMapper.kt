package com.ikvych.cocktail.presentation.mapper.user

import com.ikvych.cocktail.data.repository.model.user.UserRepoModel
import com.ikvych.cocktail.presentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.presentation.model.user.UserModel

class UserModelMapper : BaseModelMapper<UserModel, UserRepoModel>() {
    override fun mapTo(model: UserRepoModel): UserModel = with(model) {
        return UserModel (
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            avatar = avatar
        )
    }

    override fun mapFrom(model: UserModel): UserRepoModel = with(model) {
        return UserRepoModel(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            avatar = avatar
        )
    }
}