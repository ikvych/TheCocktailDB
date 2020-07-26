package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

open class DrinkViewModel(application: Application) : BaseViewModel(application) {

    private val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application)

    fun saveFavoriteDrink(drink: Drink) {
        if (drink.isFavorite()) {
            drink.setIsFavorite(false)
        } else {
            drink.setIsFavorite(true)
        }
        drinkRepository.saveDrinkIntoDb(drink)
    }
}