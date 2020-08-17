package com.ikvych.cocktail.presentation.mapper.cocktail

import com.ikvych.cocktail.data.repository.model.cocktail.IngredientRepoModel
import com.ikvych.cocktail.presentation.filter.type.*
import com.ikvych.cocktail.presentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.presentation.model.cocktail.IngredientModel
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel

class CocktailModelMapper(
    private val localizedStringModelMapper: LocalizedStringModelMapper
) : BaseModelMapper<CocktailModel, CocktailRepoModel>() {
    override fun mapFrom(model: CocktailModel) = with(model) {
        CocktailRepoModel(
            id = id,
            names = names.run(localizedStringModelMapper::mapFrom),
            category = category.key,
            alcoholType = alcoholType.key,
            glass = glass.key,
            image = image,
            instructions = instructions.run(localizedStringModelMapper::mapFrom),
            ingredients = ingredients.map {
                IngredientRepoModel(
                    it.key
                )
            },
            measures = measures,
            isFavorite = isFavorite,
            cocktailOfTheDay = cocktailOfTheDay,
            dateModified = dateModified,
            dateSaved = dateSaved
        )
    }

    override fun mapTo(model: CocktailRepoModel) = with(model) {
        CocktailModel(
            id = id,
            names = names.run(localizedStringModelMapper::mapTo),
            category = CategoryDrinkFilter.values().firstOrNull { it.key == category }
                ?: CategoryDrinkFilter.NONE,
            alcoholType = AlcoholDrinkFilter.values().firstOrNull { it.key.equals(alcoholType, ignoreCase = true) }
                ?: AlcoholDrinkFilter.NONE,
            glass = GlassDrinkFilter.values().firstOrNull { it.key == glass }
                ?: GlassDrinkFilter.NONE,
            image = image,
            instructions = instructions.run(localizedStringModelMapper::mapTo),
            ingredients = ingredients.map { IngredientModel(DrinkFilterType.INGREDIENT, it.ingredient) },
            measures = measures,
            isFavorite = isFavorite,
            cocktailOfTheDay = cocktailOfTheDay,
            dateModified = dateModified,
            dateSaved = dateSaved
        )
    }
}