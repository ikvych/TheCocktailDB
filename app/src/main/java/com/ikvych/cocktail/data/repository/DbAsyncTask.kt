package com.ikvych.cocktail.data.repository

import android.os.AsyncTask
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.entity.Drink


class FindDrinkAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Long, Unit, Drink>() {

    override fun doInBackground(vararg params: Long?): Drink {
        return drinkDao.findDrinkById(params[0]!!)
    }

}

class RemoveDrinkAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Drink, Unit, Unit>() {

    override fun doInBackground(vararg params: Drink?) {
        return drinkDao.delete(params[0]!!)
    }

}

class FindDrinkByNameAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<String, Unit, Drink>() {

    override fun doInBackground(vararg params: String?): Drink {
        return drinkDao.findDrinkByName(params[0]!!)
    }

}