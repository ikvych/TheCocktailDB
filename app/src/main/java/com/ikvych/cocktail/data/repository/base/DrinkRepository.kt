package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.db.model.Drink

interface DrinkRepository : BaseRepository{

    // Methods for work with Api
    fun getDrinkApiLiveData(): MutableLiveData<List<Drink>>

    fun updateDrinkApiLiveData(query: String)


    // Methods for work with Db
    fun getAllDrinksFromDbLiveData(): LiveData<List<Drink>>

    fun findDrinkLiveDataById(drinkId: Long): LiveData<Drink?>

    fun saveDrinkIntoDb(drink: Drink)

    fun getAllDrinksFromDb(): List<Drink>

    fun findDrinkById(drinkId: Long): Drink?

    fun findDrinkByName(drinkName: String): Drink?

    fun findDrinkOfTheDay(stringDate: String): Drink?
}