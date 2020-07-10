package com.ikvych.cocktail.data.db.impl.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.impl.dao.DrinkDao
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.db.source.DrinkDbSource

class DrinkDbSourceImpl(
    private val drinkDao: DrinkDao
) : DrinkDbSource {

    override suspend fun insert(drink: Drink) {
        drinkDao.insert(drink)
    }


    override suspend fun findDrinkOfTheDay(stringDate: String) : Drink? {
        return drinkDao.findDrinkOfTheDay(stringDate)
    }


    override fun findDrinkLiveDataById(drinkId: Long) : LiveData<Drink?> {
        return drinkDao.findDrinkLiveDataById(drinkId)
    }


    override suspend fun findDrinkById(drinkId: Long) : Drink {
        return drinkDao.findDrinkById(drinkId)
    }


    override suspend fun findDrinkByName(drinkName: String) : Drink {
        return drinkDao.findDrinkByName(drinkName)
    }


    override suspend fun getAllDrinks() : List<Drink> {
        return drinkDao.getAllDrinks()
    }


    override suspend fun getAllDrinksLiveData() : LiveData<List<Drink>> {
        return drinkDao.getAllDrinksLiveData()
    }
}