package com.ikvych.cocktail.data.network.impl.service

import com.ikvych.cocktail.data.network.impl.service.base.BaseNetService
import com.ikvych.cocktail.data.network.model.DrinkApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailNetService : BaseNetService {

    @GET("api/json/v1/1/search.php")
    fun findCocktailsByName(@Query("s") drinkName: String?): Call<DrinkApiResponse?>
}