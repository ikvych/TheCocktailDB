package com.ikvych.cocktail.prresentation.mapper.user

import com.ikvych.cocktail.prresentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.prresentation.model.user.UserModel
import com.ikvych.cocktail.repository.model.user.UserRepoModel

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