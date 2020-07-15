package com.ikvych.cocktail.ui.model.cocktail

import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.GlassDrinkFilter
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter

data class CocktailModel(
    val id: Long = -1L,
    val names: LocalizedStringModel = LocalizedStringModel(),
    val category: CategoryDrinkFilter = CategoryDrinkFilter.NONE,
    val alcoholType: AlcoholDrinkFilter = AlcoholDrinkFilter.NONE,
    val glass: GlassDrinkFilter = GlassDrinkFilter.NONE,
    val image: String = "",
    val instructions: LocalizedStringModel = LocalizedStringModel(),
    val ingredients: List<IngredientDrinkFilter> = emptyList(),
    val measures: List<String> = emptyList(),
    val cocktailOfTheDay: String = "",
    val isFavorite: Boolean = false/*,
    val date: Date = Date()*/
)