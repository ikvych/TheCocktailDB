package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkApiRepositoryImpl
import com.ikvych.cocktail.data.repository.DrinkDbRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkApiRepository
import com.ikvych.cocktail.data.repository.base.DrinkDbRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class MainViewModel(
    application: Application
) : BaseViewModel(application) {

    private val apiRepository: DrinkApiRepository = DrinkApiRepositoryImpl(application)
    private val dbRepository: DrinkDbRepository = DrinkDbRepositoryImpl(application)
    private val drinksLiveData: LiveData<List<Drink>> = dbRepository.getDrinks()
    private val favoriteDrinksLiveData: LiveData<List<Drink>> = dbRepository.getFavoriteDrinks()

    override fun getCurrentData(): List<Drink> {
        val drinkApiService = drinksLiveData.value
        return drinkApiService ?: emptyList()
    }

    fun getFavoriteCurrentData(): List<Drink> {
        return favoriteDrinksLiveData.value ?: emptyList()
    }

    fun findDrinkById(drinkId: Long): Drink {
        return dbRepository.findDrinkById(drinkId)
    }

    fun findDrinkByName(drinkName: String): Drink {
        return dbRepository.findDrinkByName(drinkName)
    }

    override fun saveDrink(drink: Drink) {
        dbRepository.saveDrink(drink)
    }

    fun removeDrink(drink: Drink) {
        dbRepository.removeDrink(drink)
    }


    override fun getLiveData(): LiveData<List<Drink>> {
        return drinksLiveData
    }

    fun getFavoriteLiveData(): LiveData<List<Drink>> {
        return favoriteDrinksLiveData
    }
}