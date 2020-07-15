package com.ikvych.cocktail.ui.mapper

import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.GlassDrinkFilter
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.ui.mapper.base.BaseModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
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
            ingredients = ingredients.map { it.key },
            measures = measures,
            isFavorite = isFavorite,
            cocktailOfTheDay = cocktailOfTheDay
        )
    }

    override fun mapTo(model: CocktailRepoModel) = with(model) {
        CocktailModel(
            id = id,
            names = names.run(localizedStringModelMapper::mapTo),
            category = CategoryDrinkFilter.values().firstOrNull { it.key == category }
                ?: CategoryDrinkFilter.NONE,
            alcoholType = AlcoholDrinkFilter.values().firstOrNull { it.key == alcoholType }
                ?: AlcoholDrinkFilter.NONE,
            glass = GlassDrinkFilter.values().firstOrNull { it.key == glass }
                ?: GlassDrinkFilter.NONE,
            image = image,
            instructions = instructions.run(localizedStringModelMapper::mapTo),
            ingredients = ingredients.map { ingredient ->
                IngredientDrinkFilter.values().firstOrNull { it.key == ingredient }
                    ?: IngredientDrinkFilter.NONE
            },
            measures = measures,
            isFavorite = isFavorite,
            cocktailOfTheDay = cocktailOfTheDay
        )
    }
}