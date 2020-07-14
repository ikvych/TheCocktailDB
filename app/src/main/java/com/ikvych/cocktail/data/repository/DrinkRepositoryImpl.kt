package com.ikvych.cocktail.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.db.impl.dao.DrinkDao
import com.ikvych.cocktail.data.db.impl.DrinkDataBase
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.db.model.DrinkApiResponse
import com.ikvych.cocktail.data.network.DrinkApiService
import com.ikvych.cocktail.data.network.RetrofitInstance
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrinkRepositoryImpl (
    application: Application
) : DrinkRepository {

    private val apiService: DrinkApiService = RetrofitInstance.service
    val drinksApiLiveData: MutableLiveData<List<Drink>> = MutableLiveData()


    // Methods for work with Api
    override fun getDrinkApiLiveData(): MutableLiveData<List<Drink>> {
        return drinksApiLiveData
    }

    override fun updateDrinkApiLiveData(query: String) {
        val call: Call<DrinkApiResponse?> = apiService.getDrinksByName(query)

        call.enqueue(object : Callback<DrinkApiResponse?> {

            override fun onFailure(call: Call<DrinkApiResponse?>, t: Throwable) {
                println("Can't get drinks list from Api")
            }

            override fun onResponse(
                call: Call<DrinkApiResponse?>,
                response: Response<DrinkApiResponse?>
            ) {
                val drinkApiResponse = response.body()
                if (drinkApiResponse?.drinks != null) {
                    drinksApiLiveData.setValue(drinkApiResponse.drinks)
                } else {
                    drinksApiLiveData.setValue(emptyList())
                }
            }

        })
    }

}