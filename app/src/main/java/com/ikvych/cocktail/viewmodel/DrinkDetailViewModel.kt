package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.repository.source.DrinkRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class DrinkDetailViewModel(
    private val drinkRepository1: DrinkRepository,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    val drinkIdLiveData: MutableLiveData<Long?> = MutableLiveData()
    val drinkLiveData: MutableLiveData<Drink?> = MutableLiveData()

    fun findDrinkInDbById(drinkId: Long) {
        launchRequest(drinkLiveData)  {
            drinkRepository1.findDrinkById(drinkId)
        }
    }

    fun saveDrinkIntoDb(drink: Drink) {
        launchRequest {
            drinkRepository1.saveDrinkIntoDb(drink)
        }
    }
}