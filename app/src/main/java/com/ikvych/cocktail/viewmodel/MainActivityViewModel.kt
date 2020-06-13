package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkDbRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkDbRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class MainActivityViewModel(
    application: Application
) : BaseViewModel(application) {

    private val dbRepository: DrinkDbRepository = DrinkDbRepositoryImpl(application)
    private val drinksLiveData: LiveData<List<Drink>> = dbRepository.getDrinks()

    override fun getCurrentData(): List<Drink> {
        return drinksLiveData.value ?: emptyList()
    }

    fun saveDrink(drink: Drink) {
        dbRepository.saveDrink(drink)
    }

    override fun getLiveData(): LiveData<List<Drink>> {
        return drinksLiveData
    }
}