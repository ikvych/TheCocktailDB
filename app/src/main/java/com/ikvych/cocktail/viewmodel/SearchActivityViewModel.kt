package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(application: Application) : BaseViewModel(application) {

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