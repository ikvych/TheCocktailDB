package com.ikvych.cocktail.data.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.data.repository.source.DrinkRepository

class DrinkRepositoryImpl(
    private val dbSource: DrinkDbSource
) : BaseRepositoryImpl(), DrinkRepository {

    override suspend fun getDrinkApiLiveData(): MutableLiveData<List<Drink>> {
        return MutableLiveData()
    }

    override suspend fun updateDrinkApiLiveData(query: String) {

    }

    override fun getAllDrinksFromDbLiveData(): LiveData<List<Drink>> {
        return dbSource.getAllDrinksLiveData()
    }

    override fun findDrinkLiveDataById(drinkId: Long): LiveData<Drink?> {
        return dbSource.findDrinkLiveDataById(drinkId)
    }

    override suspend fun saveDrinkIntoDb(drink: Drink) {
        dbSource.insert(drink)
    }

    override suspend fun getAllDrinksFromDb(): List<Drink> {
        return dbSource.getAllDrinks()
    }

    override suspend fun findDrinkById(drinkId: Long): Drink? {
        return dbSource.findDrinkById(drinkId)
    }

    override suspend fun findDrinkByName(drinkName: String): Drink? {
        return dbSource.findDrinkByName(drinkName)
    }

    override suspend fun findDrinkOfTheDay(stringDate: String): Drink? {
        return dbSource.findDrinkOfTheDay(stringDate)
    }
}