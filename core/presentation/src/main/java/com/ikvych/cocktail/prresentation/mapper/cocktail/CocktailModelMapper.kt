package com.ikvych.cocktail.prresentation.mapper.cocktail

import com.ikvych.cocktail.prresentation.mapper.base.BaseModelMapper
import com.ikvych.cocktail.prresentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.prresentation.model.cocktail.IngredientModel
import com.ikvych.cocktail.prresentation.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.prresentation.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.prresentation.filter.type.DrinkFilterType
import com.ikvych.cocktail.prresentation.filter.type.GlassDrinkFilter
import com.ikvych.cocktail.repository.model.cocktail.CocktailRepoModel
import com.ikvych.cocktail.repository.model.cocktail.IngredientRepoModel

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