package com.ikvych.cocktail.data.db.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.ikvych.cocktail.data.db.Table

@Entity(
    tableName = Table.COCKTAIL_MEASURE,
    primaryKeys = ["id", "measure"],
    foreignKeys = [
        ForeignKey(
            entity = CocktailDbModel::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MeasureDbModel::class,
            parentColumns = ["measure"],
            childColumns = ["measure"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class CocktailMeasureCrossRef(
    val id: Long,
    var measure: String
)