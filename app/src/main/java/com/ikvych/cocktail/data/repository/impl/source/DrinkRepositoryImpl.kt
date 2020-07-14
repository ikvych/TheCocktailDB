package com.ikvych.cocktail.data.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.network.model.Drink
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
        return dbSource.findAllCocktailsLiveData()
    }

    override fun findDrinkLiveDataById(drinkId: Long): LiveData<Drink?> {
        return dbSource.findCocktailByIdLiveData(drinkId)
    }

    override suspend fun saveDrinkIntoDb(drink: Drink) {
        dbSource.addOrReplaceCocktail(drink)
    }

    override suspend fun getAllDrinksFromDb(): List<Drink> {
        return dbSource.findAllCocktails()
    }

    override suspend fun findDrinkById(drinkId: Long): Drink? {
        return dbSource.findCocktailById(drinkId)
    }

    override suspend fun findDrinkByName(drinkName: String): Drink? {
        return dbSource.findCocktailByDefaultName(drinkName)
    }

    override suspend fun findDrinkOfTheDay(stringDate: String): Drink? {
        return dbSource.findCocktailOfTheDay(stringDate)
    }
}