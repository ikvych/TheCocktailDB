package com.ikvych.cocktail.data.repository

import android.os.AsyncTask
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient

class DbAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Unit, Unit, List<Ingredient>>() {

    override fun doInBackground(vararg params: Unit?): List<Ingredient> {
        return drinkDao.getAllIngredients()
    }

}

class DbAllDrinkAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Unit, Unit, List<Drink>>() {

    override fun doInBackground(vararg params: Unit?): List<Drink> {
        return drinkDao.getAllJustDrinks()
    }

}

class FindDrinkAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Long, Unit, Drink>() {

    override fun doInBackground(vararg params: Long?): Drink {
        return drinkDao.findDrinkById(params[0]!!)
    }

}

class FindDrinkByNameAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<String, Unit, Drink>() {

    override fun doInBackground(vararg params: String?): Drink? {
        return drinkDao.findDrinkByName(params[0]!!)
    }

}

class FindDrinkOfTheDayAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<String, Unit, Drink>() {

    override fun doInBackground(vararg params: String?): Drink? {
        return drinkDao.findDrinkOfTheDay(params[0]!!)
    }

}


class SaveIngredientsAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Ingredient, Unit, Unit>() {

    override fun doInBackground(vararg params: Ingredient) {
        params.forEach { ingredient -> drinkDao.saveIngredient(ingredient) }
    }

}

class SaveDrinkAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Drink, Unit, Unit>() {

    override fun doInBackground(vararg params: Drink) {
        params.forEach { drink ->  drinkDao.insert(drink)}
    }
}