package com.ikvych.cocktail.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ikvych.cocktail.repository.impl.mapper.user.UserRepoModelMapper
import com.ikvych.cocktail.database.source.UserDbSource
import com.ikvych.cocktail.network.model.UserNetModel
import com.ikvych.cocktail.network.source.UserNetSource
import com.ikvych.cocktail.repository.model.user.UserRepoModel
import com.ikvych.cocktail.repository.source.UserRepository
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
            .run { userModelMapper.mapRepoToDb(userModelMapper.mapNetToRepo(this)) }
            .run {
                userDbSource.saveUser(this)
            }
    }

    override suspend fun updateUser(user: UserRepoModel) {
        userNetSource.updateUser(
            UserNetModel(
                id = user.id,
                name = user.name,
                lastName = user.lastName,
                email = user.email,
                avatar = user.avatar
            )
        )
        refreshUser()
    }

    override suspend fun updateUserAvatar(avatar: File, onUploadProgress: (Float) -> Unit) {
        userNetSource.updateUserLogo(avatar) { percent, _, _ -> onUploadProgress(percent) }
        refreshUser()
    }

    override suspend fun deleteUser() {
        userDbSource.deleteUser()
    }
}
