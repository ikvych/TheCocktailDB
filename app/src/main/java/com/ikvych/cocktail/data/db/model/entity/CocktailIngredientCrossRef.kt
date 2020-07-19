package com.ikvych.cocktail.data.db.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
    primaryKeys = ["id", "ingredient"],
    foreignKeys = [
        ForeignKey(
            entity = CocktailDbModel::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = IngredientDbModel::class,
            parentColumns = ["ingredient"],
            childColumns = ["ingredient"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class CocktailIngredientCrossRef(
    val id: Long,
    var ingredient: String
)