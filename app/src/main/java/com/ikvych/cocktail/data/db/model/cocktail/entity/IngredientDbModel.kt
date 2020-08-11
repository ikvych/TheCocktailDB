package com.ikvych.cocktail.data.db.model.cocktail.entity

import androidx.room.*
import com.ikvych.cocktail.data.db.Table

@Entity(tableName = Table.INGREDIENT)
data class IngredientDbModel(
    @PrimaryKey
    @ColumnInfo(name = "ingredient")
    var ingredient: String
)