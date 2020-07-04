package com.ikvych.cocktail.data.repository

import android.os.AsyncTask
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient

class FindAllDrinksAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Unit, Unit, List<Drink>>() {

    override fun doInBackground(vararg params: Unit?): List<Drink> {
        return drinkDao.getAllDrinks()
    }
}

class FindAllIngredientAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Unit, Unit, List<Ingredient>>() {

    override fun doInBackground(vararg params: Unit?): List<Ingredient> {
        return drinkDao.getAllIngredients()
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