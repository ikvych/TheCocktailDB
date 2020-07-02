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


    // Methods for work with Api and Db at same time
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


    // Methods for work with Db
    override fun getDrinkDbLiveData(): LiveData<List<Drink>> {
        return drinkDao.getAllDrinks()
    }

    override fun saveDrinkIntoDb(drink: Drink) {
        SaveDrinkAsyncTask(drinkDao).execute(drink)
    }

    override fun getAllIngredientFromDb(): List<Ingredient> {
        return FindAllIngredientAsyncTask(drinkDao).execute().get()
    }

    override fun findDrinkById(drinkId: Long): Drink? {
        return FindDrinkByIdAsyncTask(drinkDao).execute(drinkId).get()
    }

    override fun findDrinkByName(drinkName: String): Drink {
        return FindDrinkByNameAsyncTask(drinkDao).execute(drinkName).get()
    }

    override fun findDrinkOfTheDay(stringDate: String): Drink? {
        return FindDrinkOfTheDayAsyncTask(drinkDao).execute(stringDate).get()
    }
}