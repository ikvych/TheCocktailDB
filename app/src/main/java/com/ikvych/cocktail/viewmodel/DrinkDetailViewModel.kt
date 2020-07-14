package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.repository.source.DrinkRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class DrinkDetailViewModel(
    private val drinkRepository1: DrinkRepository,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    private val triggerObserver: Observer<in Drink?> = Observer { }
    val drinkLiveData: MutableLiveData<Drink?> = MutableLiveData()

    init {
        drinkLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        drinkLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }

    fun findDrinkDbById(drinkId: Long) {
        launchRequest(drinkLiveData)  {
            drinkRepository1.findDrinkById(drinkId)
        }
    }

    fun saveDrinkIntoDb() {
        val currentDrink = drinkLiveData.value ?: return
        launchRequest {
            drinkRepository1.saveDrinkIntoDb(currentDrink)
        }
    }
}