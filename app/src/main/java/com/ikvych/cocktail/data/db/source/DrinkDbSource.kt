package com.ikvych.cocktail.data.db.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.model.Drink

interface DrinkDbSource : BaseDbSource{

    suspend fun insert(drink: Drink)
    suspend fun findDrinkOfTheDay(stringDate: String) : Drink?
    fun findDrinkLiveDataById(drinkId: Long) : LiveData<Drink?>
    suspend fun findDrinkById(drinkId: Long) : Drink
    suspend fun findDrinkByName(drinkName: String) : Drink
    suspend fun getAllDrinks() : List<Drink>
    suspend fun getAllDrinksLiveData() : LiveData<List<Drink>>

}