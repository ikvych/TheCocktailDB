package com.ikvych.cocktail.data.repository

import android.os.AsyncTask
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.entity.Drink

class FindAllDrinksAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Unit, Unit, List<Drink>>() {

    override fun doInBackground(vararg params: Unit?): List<Drink> {
        return drinkDao.getAllDrinks()
    }
}

class FindDrinkByIdAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Long, Unit, Drink>() {

    override fun doInBackground(vararg params: Long?): Drink? {
        val firstElement = params[0] ?: return null
        return drinkDao.findDrinkById(firstElement)
    }
}

class FindDrinkByNameAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<String, Unit, Drink>() {

    override fun doInBackground(vararg params: String?): Drink? {
        val firstElement = params[0] ?: return null
        return drinkDao.findDrinkByName(firstElement)
    }
}

class FindDrinkOfTheDayAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<String, Unit, Drink>() {

    override fun doInBackground(vararg params: String?): Drink? {
        val firstElement = params[0] ?: return null
        return drinkDao.findDrinkOfTheDay(firstElement)
    }
}

class SaveDrinkAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Drink, Unit, Unit>() {

    override fun doInBackground(vararg params: Drink) {
        params.forEach { drink ->  drinkDao.insert(drink)}
    }
}

class RemoveDrinkAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Drink, Unit, Unit>() {

    override fun doInBackground(vararg params: Drink?) {
        return drinkDao.delete(params[0]!!)
    }

}