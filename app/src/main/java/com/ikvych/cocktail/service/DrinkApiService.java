package com.ikvych.cocktail.service;

import com.ikvych.cocktail.model.DrinkApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DrinkApiService {

    @GET("api/json/v1/1/search.php")
    Call<DrinkApiResponse> getDrinksByName(@Query("s") String drinkName);
}
