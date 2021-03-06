package com.xtreeivi.cocktailsapp.data.repository.model

import com.ikvych.cocktail.data.repository.model.cocktail.IngredientRepoModel
import java.util.*

data class CocktailRepoModel(
    val id: Long = -1L,
    val names: LocalizedStringRepoModel = LocalizedStringRepoModel(),
    val category: String = "",
    val alcoholType: String = "",
    val glass: String = "",
    val image: String = "",
    val instructions: LocalizedStringRepoModel = LocalizedStringRepoModel(),
    val ingredients: List<IngredientRepoModel> = emptyList(),
    val measures: List<String> = emptyList(),
    var isFavorite: Boolean = false,
    var cocktailOfTheDay: String = "",
    val dateModified: Date = Date(),
    val dateSaved: Date? = null
)