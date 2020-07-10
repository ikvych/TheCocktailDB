package com.ikvych.cocktail.data.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.database.DrinkDao
import com.ikvych.cocktail.data.database.DrinkDataBase
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.base.DrinkDbRepository


class DrinkDbRepositoryImpl(context: Context):
    DrinkDbRepository {

    private val drinkDao: DrinkDao = DrinkDataBase.getInstance(context)!!.drinkDao()


    override fun getDrinks(): LiveData<List<Drink>> {
        return drinkDao.getAllDrinks()
    }

    override fun getFavoriteDrinks(): LiveData<List<Drink>> {
        return drinkDao.getAllFavoriteDrinks()
    }

    override fun saveDrink(drink: Drink) {
        SaveDrinkAsyncTask(drinkDao).execute(drink)
    }

    override fun findDrinkById(drinkId: Long): Drink {
        return FindDrinkAsyncTask(drinkDao).execute(drinkId).get()
    }

    override fun findDrinkByName(drinkName: String): Drink {
        return FindDrinkByNameAsyncTask(drinkDao).execute(drinkName).get()
    }

    private companion object class SaveDrinkAsyncTask(private val drinkDao: DrinkDao): AsyncTask<Drink, Unit, Unit>() {

        override fun doInBackground(vararg params: Drink) {
            params.forEach { drink ->  drinkDao.insert(drink)}
        }
    }
}