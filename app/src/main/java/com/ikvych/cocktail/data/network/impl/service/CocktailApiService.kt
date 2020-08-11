package com.ikvych.cocktail.data.network.impl.service

import com.ikvych.cocktail.data.network.impl.service.base.BaseNetService
import com.ikvych.cocktail.data.network.model.cocktail.CocktailNetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApiService : BaseNetService {

    @GET("api/json/v1/1/search.php")
    suspend fun findCocktailsByName(@Query("s") drinkName: String?): CocktailNetResponse
}