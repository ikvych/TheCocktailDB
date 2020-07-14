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
    val drinkIdLiveData: MutableLiveData<Long> = MutableLiveData()
    val drinkLiveData: MutableLiveData<Drink?> = object : MediatorLiveData<Drink?>() {
        init {
            addSource(drinkIdLiveData) {
                value = findDrinkInDbById(it)
            }
        }
    }

    init {
        drinkLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        drinkLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }

    fun findDrinkInDbById(drinkId: Long): Drink? {
        return drinkRepository.findDrinkById(drinkId)
    }

    fun saveDrinkIntoDb() {
        val drink = drinkLiveData.value ?: return
        drinkRepository.saveDrinkIntoDb(drink)
    }
}