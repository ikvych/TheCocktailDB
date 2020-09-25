package com.ikvych.cocktail.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikvych.cocktail.database.Table
import com.ikvych.cocktail.database.model.notification.NotificationDbModel
import com.ikvych.cocktail.impl.dao.base.BaseDao

@Dao
interface NotificationDao : BaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNotification(model: NotificationDbModel)

    @Query("DELETE FROM ${Table.NOTIFICATION}")
    suspend fun deleteNotification()

    @Query("SELECT * FROM ${Table.NOTIFICATION} LIMIT 1")
    suspend fun getNotification(): NotificationDbModel?
}