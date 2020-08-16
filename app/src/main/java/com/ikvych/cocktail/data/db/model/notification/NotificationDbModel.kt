package com.ikvych.cocktail.data.db.model.notification

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikvych.cocktail.data.db.Table

@Entity(tableName = Table.NOTIFICATION)
data class NotificationDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = 1L,
    @ColumnInfo(name = "type")
    val type: String = "",
    @ColumnInfo(name = "image")
    val image: String? = "",
    @ColumnInfo(name = "cocktailId")
    val cocktailId: Long? = -1L
)