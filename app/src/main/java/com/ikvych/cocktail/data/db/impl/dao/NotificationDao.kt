package com.ikvych.cocktail.data.db.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikvych.cocktail.data.db.Table
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.model.notification.NotificationDbModel

@Dao
interface NotificationDao : BaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNotification(model: NotificationDbModel)

    @Query("DELETE FROM ${Table.NOTIFICATION}")
    suspend fun deleteNotification()

    @Query("SELECT * FROM ${Table.NOTIFICATION} LIMIT 1")
    suspend fun getNotification(): NotificationDbModel?
}