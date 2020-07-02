package com.ikvych.cocktail.viewmodel

import android.app.Application
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class DrinkDetailViewModel(application: Application) : BaseViewModel(application){

    fun findDrinkInDbById(drinkId: Long): Drink? {
        return drinkRepository.findDrinkById(drinkId)
    }

    fun saveDrinkIntoDb(drink: Drink) {
        drinkRepository.saveDrinkIntoDb(drink)
    }
}