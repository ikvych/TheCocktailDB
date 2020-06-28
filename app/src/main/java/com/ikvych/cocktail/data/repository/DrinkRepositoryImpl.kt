package com.ikvych.cocktail.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.database.DrinkDataBase
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.DrinkApiResponse
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.data.entity.IngredientApiResponse
import com.ikvych.cocktail.data.network.DrinkApiService
import com.ikvych.cocktail.data.network.RetrofitInstance
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrinkRepositoryImpl (application: Application) : DrinkRepository {

    private val drinkDao: DrinkDao = DrinkDataBase.getInstance(application)!!.drinkDao()
    private val apiService: DrinkApiService = RetrofitInstance.service
    val drinksLiveData: MutableLiveData<List<Drink>> = MutableLiveData()

    override fun updateDrinksLiveData(query: String) {
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
                println("Can't get ingredients from Api")
            }

            override fun onResponse(
                call: Call<IngredientApiResponse?>,
                response: Response<IngredientApiResponse?>
            ) {
                val drinkApiResponse = response.body()
                if (drinkApiResponse?.ingredients != null) {
                    SaveIngredientsAsyncTask(drinkDao).execute(*drinkApiResponse.ingredients.toTypedArray())
                }
            }

        })
    }

    override fun getDrinks(): LiveData<List<Drink>> {
        return drinkDao.getAllDrinks()
    }

    override fun getFavoriteDrinks(): LiveData<List<Drink>> {
        return drinkDao.getAllFavoriteDrinks()
    }

    override fun saveDrink(drink: Drink) {
        SaveDrinkAsyncTask(drinkDao).execute(drink)
    }

    override fun getAllIngredient(): List<Ingredient> {
        return DbAsyncTask(drinkDao).execute().get()
    }

    override fun findDrinkById(drinkId: Long): Drink {
        return FindDrinkAsyncTask(drinkDao).execute(drinkId).get()
    }

    override fun findDrinkByName(drinkName: String): Drink {
        return FindDrinkByNameAsyncTask(drinkDao).execute(drinkName).get()
    }

    override fun getLiveData(): MutableLiveData<List<Drink>> {
        return drinksLiveData
    }

    override fun getCurrentData(): List<Drink> {
        return drinksLiveData.value ?: emptyList()
    }
}