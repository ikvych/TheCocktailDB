package com.ikvych.cocktail.ui.model.cocktail

import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType

data class IngredientModel(
    override val type: DrinkFilterType,
    override val key: String
) : DrinkFilter