package com.ikvych.cocktail.data.network

import com.ikvych.cocktail.data.entity.DrinkApiResponse
import com.ikvych.cocktail.data.entity.IngredientApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DrinkApiService {

    @GET("api/json/v1/1/search.php")
    fun getDrinksByName(@Query("s") drinkName: String?): Call<DrinkApiResponse?>

    @GET("api/json/v1/1/list.php?i=list")
    fun getAllIngredients(): Call<IngredientApiResponse?>
}