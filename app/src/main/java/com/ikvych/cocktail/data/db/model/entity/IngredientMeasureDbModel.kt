package com.ikvych.cocktail.data.db.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.ikvych.cocktail.data.db.Table

@Entity(
    tableName = Table.INGREDIENT_MEASURE,
    primaryKeys = ["cocktail", "ingredient", "measure"],
    foreignKeys = [
        ForeignKey(
            entity = CocktailDbModel::class,
            parentColumns = ["id"],
            childColumns = ["cocktail"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class IngredientMeasureDbModel(

    @ColumnInfo(name = "cocktail")
    val cocktail: Long,

    @ColumnInfo(name = "ingredient")
    val ingredient: String,

    @ColumnInfo(name = "measure")
    val measure: String
)

