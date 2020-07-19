package com.ikvych.cocktail.data.db.model.entity

import androidx.room.Entity

@Entity(primaryKeys = ["id", "ingredient"])
data class CocktailIngredientCrossRef(
    val id: Long,
    var ingredient: String
)