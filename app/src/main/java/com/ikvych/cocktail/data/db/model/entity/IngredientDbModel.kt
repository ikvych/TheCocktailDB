package com.ikvych.cocktail.data.db.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikvych.cocktail.data.db.Table

@Entity(tableName = Table.INGREDIENT)
data class IngredientDbModel(
    @PrimaryKey
    @ColumnInfo(name = "ingredient")
    var ingredient: String
)