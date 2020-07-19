package com.ikvych.cocktail.data.db.model.entity

import androidx.room.Entity

@Entity(primaryKeys = ["id", "measure"])
data class CocktailMeasureCrossRef(
    val id: Long,
    var measure: String
)