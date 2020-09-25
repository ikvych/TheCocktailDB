package com.ikvych.cocktail.prresentation.model.cocktail

import com.ikvych.cocktail.prresentation.filter.DrinkFilter
import com.ikvych.cocktail.prresentation.filter.type.DrinkFilterType

data class IngredientModel(
    override val type: DrinkFilterType,
    override val key: String
) : DrinkFilter