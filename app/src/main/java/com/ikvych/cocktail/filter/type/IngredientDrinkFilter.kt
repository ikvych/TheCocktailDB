package com.ikvych.cocktail.filter.type

import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.filter.DrinkFilter

enum class IngredientDrinkFilter(
    override val type: DrinkFilterType,
    override var key: String
) : DrinkFilter {
    INGREDIENT(DrinkFilterType.INGREDIENT, "None")
}