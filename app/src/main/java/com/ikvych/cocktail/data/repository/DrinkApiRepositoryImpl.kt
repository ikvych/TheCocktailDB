package com.ikvych.cocktail.data.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.database.DrinkDataBase
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.DrinkApiResponse
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.data.entity.IngredientApiResponse
import com.ikvych.cocktail.data.network.DrinkApiService
import com.ikvych.cocktail.data.network.RetrofitInstance
import com.ikvych.cocktail.data.repository.base.DrinkApiRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DrinkApiRepositoryImpl(context: Context) :
    DrinkApiRepository {

    private val apiService: DrinkApiService = RetrofitInstance.service
    val drinksLiveData: MutableLiveData<List<Drink>> = MutableLiveData()

    private val drinkDao: DrinkDao = DrinkDataBase.getInstance(context)!!.drinkDao()

    override fun updateDrinksLiveData(query: String) {
        val call: Call<DrinkApiResponse?> = apiService.getDrinksByName(query)

        call.enqueue(object : Callback<DrinkApiResponse?> {

            override fun onFailure(call: Call<DrinkApiResponse?>, t: Throwable) {
                println("Jello")
            }

            override fun onResponse(
                call: Call<DrinkApiResponse?>,
                response: Response<DrinkApiResponse?>
            ) {
                val drinkApiResponse = response.body()
                if (drinkApiResponse?.drinks != null) {
                    drinksLiveData.setValue(drinkApiResponse.drinks)
                } else {
                    drinksLiveData.setValue(emptyList())
                }
            }

        })
    }

    override fun initAllIngredient() {
        val call: Call<IngredientApiResponse?> = apiService.getAllIngredients()

        call.enqueue(object : Callback<IngredientApiResponse?> {

            override fun onFailure(call: Call<IngredientApiResponse?>, t: Throwable) {
                println("Jello")
            }

            override fun onResponse(
                call: Call<IngredientApiResponse?>,
                response: Response<IngredientApiResponse?>
            ) {
                val drinkApiResponse = response.body()
                if (drinkApiResponse?.ingredients != null) {
                    val noneIngredient = Ingredient().apply {
                        id = 0
                        strIngredient1 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                    }
                    SaveIngredientsAsyncTask(drinkDao).execute(*drinkApiResponse.ingredients.toTypedArray(), noneIngredient)
                }
            }

        })
    }

    override fun getLiveData(): MutableLiveData<List<Drink>> {
        return drinksLiveData
    }

    override fun getCurrentData(): List<Drink> {
        return drinksLiveData.value ?: emptyList()
    }

    private companion object
    class SaveIngredientsAsyncTask(private val drinkDao: DrinkDao) :
        AsyncTask<Ingredient, Unit, Unit>() {

        override fun doInBackground(vararg params: Ingredient) {
            drinkDao.clearTable()
            params.forEach { ingredient -> drinkDao.saveIngredient(ingredient) }
        }

    }
}