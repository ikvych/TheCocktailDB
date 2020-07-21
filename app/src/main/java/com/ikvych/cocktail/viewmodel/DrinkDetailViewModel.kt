package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class DrinkDetailViewModel(application: Application) : BaseViewModel(application) {

    private val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application)
    private val triggerObserver: Observer<in Drink?> = Observer { }
    val drinkIdLiveData: MutableLiveData<Long> = MutableLiveData()
    val drinkLiveData: MutableLiveData<Drink?> = object : MediatorLiveData<Drink?>() {
        init {
            addSource(drinkIdLiveData) {
                value = drinkRepository.findDrinkById(it)
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

    fun saveDrinkIntoDb() {
        val drink = drinkLiveData.value ?: return
        drinkRepository.saveDrinkIntoDb(drink)
    }
}