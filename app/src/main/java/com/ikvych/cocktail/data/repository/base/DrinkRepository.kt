package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient

interface DrinkRepository {

    // Methods for work with Api
    fun getApiLiveData(): MutableLiveData<List<Drink>>

    fun updateDrinksApiLiveData(query: String)

    // Methods for work with Api and Db at same time
    fun initAllIngredient()


    // Methods for work with Db
    fun getJustDrinks(): List<Drink>

    fun getDrinks(): LiveData<List<Drink>>

    fun getFavoriteDrinks(): LiveData<List<Drink>>

    fun saveDrink(drink: Drink)

    fun getAllIngredient(): List<Ingredient>

    fun findDrinkById(drinkId: Long): Drink

    fun findDrinkOfTheDay(stringDate: String): Drink?

    fun findDrinkByName(drinkName: String): Drink
}