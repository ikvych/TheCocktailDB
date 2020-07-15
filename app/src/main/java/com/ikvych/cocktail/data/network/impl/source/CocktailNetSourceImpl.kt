package com.ikvych.cocktail.data.network.impl.source

import com.ikvych.cocktail.data.network.impl.service.CocktailNetService
import com.ikvych.cocktail.data.network.model.CocktailNetResponse
import com.ikvych.cocktail.data.network.model.DrinkApiResponse
import com.ikvych.cocktail.data.network.source.CocktailNetSource
import retrofit2.Call

class CocktailNetSourceImpl(
    private val netService: CocktailNetService
) : CocktailNetSource {

    override fun getCocktailsByName(cocktailName: String): Call<CocktailNetResponse?> {
        return netService.findCocktailsByName(cocktailName)
    }

}