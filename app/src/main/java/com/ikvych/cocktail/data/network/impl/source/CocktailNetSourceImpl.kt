package com.ikvych.cocktail.data.network.impl.source

import com.ikvych.cocktail.data.network.impl.service.CocktailApiService
import com.ikvych.cocktail.data.network.impl.source.base.BaseNetSourceImpl
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetResponse
import com.ikvych.cocktail.data.network.source.CocktailNetSource
import retrofit2.Call

class CocktailNetSourceImpl(
    cocktailApiService: CocktailApiService
) : BaseNetSourceImpl<CocktailApiService>(cocktailApiService), CocktailNetSource {

    override suspend fun getCocktailsByName(cocktailName: String): List<CocktailNetModel>? {
        return performRequest {
            findCocktailsByName(cocktailName).cocktails
        }
    }

    override suspend fun getCocktailById(cocktailId: Long): CocktailNetModel? {
        return performRequest {
            findCocktailById(cocktailId).cocktails.firstOrNull()
        }
    }

}