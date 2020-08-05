package com.ikvych.cocktail.data.repository.impl.mapper.user

import com.ikvych.cocktail.data.db.model.user.entity.UserDbModel
import com.ikvych.cocktail.data.network.model.UserNetModel
import com.ikvych.cocktail.data.repository.impl.mapper.base.BaseRepoModelMapper
import com.ikvych.cocktail.data.repository.model.user.UserRepoModel

class UserRepoModelMapper : BaseRepoModelMapper<UserRepoModel, UserDbModel, UserNetModel>() {

    override fun mapDbToRepo(db: UserDbModel): UserRepoModel = with(db) {
        return UserRepoModel(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            avatar = avatar
        )
    }

    override fun mapRepoToDb(repo: UserRepoModel): UserDbModel = with(repo) {
        return UserDbModel(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            avatar = avatar
        )
    }

    override fun mapNetToRepo(net: UserNetModel): UserRepoModel = with(net) {
        return UserRepoModel(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            avatar = avatar ?: ""
        )
    }

    override fun mapRepoToNet(repo: UserRepoModel): UserNetModel = with(repo) {
        return UserNetModel(
            id = id,
            name = name,
            lastName = lastName,
            email = email,
            avatar = avatar
        )
    }
}