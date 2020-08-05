package com.ikvych.cocktail.data.network.source

import com.ikvych.cocktail.data.network.model.CocktailNetResponse
import retrofit2.Call

interface CocktailNetSource : BaseNetSource {
    fun getCocktailsByName(cocktailName: String): Call<CocktailNetResponse?>
}