package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(application: Application) : DrinkViewModel(application) {

    private val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application)
    val drinkLiveData: MutableLiveData<List<Drink>> =
        object : MediatorLiveData<List<Drink>>() {
            init {
                addSource(drinkRepository.getDrinkApiLiveData()) {
                    value = drinkRepository.getDrinkApiLiveData().value
                }
            }
        }

    val isDrinkLiveDataInitialized: LiveData<Boolean> = object : MediatorLiveData<Boolean>() {
        init {
            value = false
            addSource(drinkLiveData) {
                value = true
            }
        }
    }

    fun updateDrinksLiveData(query: String) {
        drinkRepository.updateDrinkApiLiveData(query)
    }
}