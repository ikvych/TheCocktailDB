package com.ikvych.cocktail.data.network.source

import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetResponse
import com.ikvych.cocktail.data.network.source.base.BaseNetSource
import retrofit2.Call

interface CocktailNetSource :
    BaseNetSource {
    suspend fun getCocktailsByName(cocktailName: String): List<CocktailNetModel>
}