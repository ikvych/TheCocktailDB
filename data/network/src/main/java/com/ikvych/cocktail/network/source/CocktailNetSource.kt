package com.ikvych.cocktail.network.source

import com.ikvych.cocktail.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.network.source.base.BaseNetSource


interface CocktailNetSource :
    BaseNetSource {
    suspend fun getCocktailsByName(cocktailName: String): List<CocktailNetModel>?
    suspend fun getCocktailById(cocktailId: Long): CocktailNetModel?
}