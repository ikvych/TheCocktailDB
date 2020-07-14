package com.ikvych.cocktail.data.repository.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.repository.source.base.BaseRepository

interface DrinkRepository : BaseRepository{


    // Methods for work with Api
    suspend fun getDrinkApiLiveData(): MutableLiveData<List<Drink>>

    suspend fun updateDrinkApiLiveData(query: String)

    // Methods for work with Db
    fun getAllDrinksFromDbLiveData(): LiveData<List<Drink>>

    fun findDrinkLiveDataById(drinkId: Long): LiveData<Drink?>

    suspend fun saveDrinkIntoDb(drink: Drink)

    suspend fun getAllDrinksFromDb(): List<Drink>

    suspend fun findDrinkById(drinkId: Long): Drink?

    suspend fun findDrinkByName(drinkName: String): Drink?

    suspend fun findDrinkOfTheDay(stringDate: String): Drink?
}