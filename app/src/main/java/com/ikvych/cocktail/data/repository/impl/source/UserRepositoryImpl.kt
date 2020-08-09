package com.ikvych.cocktail.data.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ikvych.cocktail.data.db.source.UserDbSource
import com.ikvych.cocktail.data.network.model.UserNetModel
import com.ikvych.cocktail.data.network.source.UserNetSource
import com.ikvych.cocktail.data.repository.impl.mapper.user.UserRepoModelMapper
import com.ikvych.cocktail.data.repository.model.user.UserRepoModel
import com.ikvych.cocktail.data.repository.source.UserRepository
import java.io.File

class UserRepositoryImpl(
    private val userDbSource: UserDbSource,
    private val userNetSource: UserNetSource,
    private val userModelMapper: UserRepoModelMapper
) : UserRepository {

    override val userLiveData: LiveData<UserRepoModel?> = userDbSource.userLiveData
        .map {
            when {
                it != null -> userModelMapper.mapDbToRepo(it)
                else -> null
            }
        }


    override suspend fun getUser() = userDbSource.getUser()?.run(userModelMapper::mapDbToRepo)

    override suspend fun refreshUser() {
        userNetSource.getUser()
            .run{userModelMapper.mapRepoToDb(userModelMapper.mapNetToRepo(this))}
            .run {
                userDbSource.saveUser(this)
            }
    }

    override suspend fun updateUser(user: UserRepoModel) {
        userDbSource.saveUser(user.run(userModelMapper::mapRepoToDb))
    }

    override suspend fun updateUserOnServer(user: UserRepoModel) {
        userNetSource.updateUser(UserNetModel(
            id = user.id,
            name = user.name,
            lastName = user.lastName,
            email = user.email,
            avatar = user.avatar
        ))
        refreshUser()
    }

    override suspend fun updateUserLogo(avatar: File) {
        userNetSource.updateUserLogo(avatar)
        refreshUser()
    }

    override suspend fun deleteUser() {
        userDbSource.deleteUser()
    }


}
