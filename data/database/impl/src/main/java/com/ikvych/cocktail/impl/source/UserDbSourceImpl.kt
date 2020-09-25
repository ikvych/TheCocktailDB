package com.ikvych.cocktail.impl.source

import com.ikvych.cocktail.database.model.user.entity.UserDbModel
import com.ikvych.cocktail.database.source.UserDbSource
import com.ikvych.cocktail.impl.dao.UserDao

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