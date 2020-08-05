package com.ikvych.cocktail.data.db.impl.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.impl.dao.UserDao
import com.ikvych.cocktail.data.db.model.user.entity.UserDbModel
import com.ikvych.cocktail.data.db.source.UserDbSource

class UserDbSourceImpl(
    private val userDao: UserDao
) : UserDbSource {

    override val userLiveData = userDao.userLiveData

    override suspend fun getUser(): UserDbModel? {
        return userDao.getUser()
    }

    override suspend fun addOrReplaceUser(user: UserDbModel) {
        userDao.addOrReplaceUser(user)
    }

    override suspend fun saveUser(user: UserDbModel) {
        userDao.saveUser(user)
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }
}