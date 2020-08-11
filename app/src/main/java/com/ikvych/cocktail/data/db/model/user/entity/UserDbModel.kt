package com.ikvych.cocktail.data.db.model.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikvych.cocktail.data.db.Table

@Entity(tableName = Table.USER)
class UserDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = 1L,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "last_name")
    val lastName: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "avatar")
    val avatar: String = ""
)