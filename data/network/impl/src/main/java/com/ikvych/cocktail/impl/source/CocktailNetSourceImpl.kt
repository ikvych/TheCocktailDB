package com.ikvych.cocktail.impl.source

import com.ikvych.cocktail.impl.service.CocktailApiService
import com.ikvych.cocktail.impl.source.base.BaseNetSourceImpl
import com.ikvych.cocktail.network.model.cocktail.CocktailNetModel
import com.ikvych.cocktail.network.source.CocktailNetSource

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