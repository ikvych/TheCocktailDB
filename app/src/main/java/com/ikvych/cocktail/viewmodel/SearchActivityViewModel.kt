package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.repository.source.DrinkRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(
    private val drinkRepository1: DrinkRepository,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    val drinkLiveData: MutableLiveData<List<Drink>> =
        object : MediatorLiveData<List<Drink>>() {
            init {
                addSource(drinkRepository.getDrinkApiLiveData()) {
                    value = drinkRepository.getDrinkApiLiveData().value
                }
            }
        }

    fun updateDrinksLiveData(query: String) {
        drinkRepository.updateDrinkApiLiveData(query)
    }
}