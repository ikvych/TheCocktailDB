package com.ikvych.cocktail.data.network.impl

import com.ikvych.cocktail.data.network.impl.service.CocktailNetService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://www.thecocktaildb.com/"
    val service: CocktailNetService
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(CocktailNetService::class.java)
        }
}