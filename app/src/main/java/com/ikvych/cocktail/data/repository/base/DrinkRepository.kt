package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient

interface DrinkRepository {

    // Methods for work with Api
    fun getDrinkApiLiveData(): MutableLiveData<List<Drink>>

    fun updateDrinkApiLiveData(query: String)

    // Methods for work with Api and Db at same time
    fun initAllIngredient()

    // Methods for work with Db
    fun getAllDrinksFromDbLiveData(): LiveData<List<Drink>>

    fun saveDrinkIntoDb(drink: Drink)

    fun getAllIngredientFromDb(): List<Ingredient>

    fun getAllDrinksFromDb(): List<Drink>

    fun findDrinkById(drinkId: Long): Drink?

    fun findDrinkByName(drinkName: String): Drink?

    fun findDrinkOfTheDay(stringDate: String): Drink?
}