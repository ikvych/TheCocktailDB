package com.ikvych.cocktail.presentation.model.cocktail

import com.ikvych.cocktail.presentation.filter.DrinkFilter
import com.ikvych.cocktail.presentation.filter.type.DrinkFilterType

data class IngredientModel(
    override val type: DrinkFilterType,
    override val key: String
) : DrinkFilter