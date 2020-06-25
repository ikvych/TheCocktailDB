package com.ikvych.cocktail.data.repository

import android.os.AsyncTask
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.entity.Ingredient

class DbAsyncTask(private val drinkDao: DrinkDao) :
    AsyncTask<Unit, Unit, List<Ingredient>>() {

    override fun doInBackground(vararg params: Unit?): List<Ingredient> {
        return drinkDao.getAllIngredients()
    }

}