package com.ikvych.cocktail.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ikvych.cocktail.database.Table
import com.ikvych.cocktail.database.model.user.entity.UserDbModel
import com.ikvych.cocktail.impl.dao.base.BaseDao

@Dao
interface UserDao : BaseDao {

    @get:Query("SELECT * FROM ${Table.USER}")
    val userLiveData: LiveData<UserDbModel?>

    @Query("SELECT * FROM ${Table.USER} LIMIT 1")
    suspend fun getUser(): UserDbModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrReplaceUser(user: UserDbModel)

    @Transaction
    suspend fun saveUser(user: UserDbModel) {
        deleteUser()
        addOrReplaceUser(user)
    }

    @Query("DELETE FROM ${Table.USER}")
    suspend fun deleteUser()

}