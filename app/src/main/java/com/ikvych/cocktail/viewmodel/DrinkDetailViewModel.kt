package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class DrinkDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    val drinkIdLiveData: MutableLiveData<Long?> = MutableLiveData()
    val drinkLiveData: LiveData<Drink?> = object : MediatorLiveData<Drink?>() {
        init {
            addSource(drinkIdLiveData) {
                value = drinkRepository.findDrinkById(drinkIdLiveData.value!!)
            }
        }
    }

    fun findDrinkInDbById(drinkId: Long): Drink? {
        return drinkRepository.findDrinkById(drinkId)
    }

    fun saveDrinkIntoDb(drink: Drink) {
        drinkRepository.saveDrinkIntoDb(drink)
    }
}