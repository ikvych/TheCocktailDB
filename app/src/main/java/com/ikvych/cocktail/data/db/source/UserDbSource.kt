package com.ikvych.cocktail.data.db.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.model.user.entity.UserDbModel
import com.ikvych.cocktail.data.db.source.base.BaseDbSource

interface UserDbSource : BaseDbSource{
    val userLiveData: LiveData<UserDbModel?>

    suspend fun getUser(): UserDbModel?
    suspend fun addOrReplaceUser(user: UserDbModel)
    suspend fun saveUser(user: UserDbModel)
    suspend fun deleteUser()
}