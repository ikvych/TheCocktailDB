package com.ikvych.cocktail.database.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.database.model.user.entity.UserDbModel
import com.ikvych.cocktail.database.source.base.BaseDbSource

interface UserDbSource : BaseDbSource {
    val userLiveData: LiveData<UserDbModel?>

    suspend fun getUser(): UserDbModel?
    suspend fun addOrReplaceUser(user: UserDbModel)
    suspend fun saveUser(user: UserDbModel)
    suspend fun deleteUser()
}