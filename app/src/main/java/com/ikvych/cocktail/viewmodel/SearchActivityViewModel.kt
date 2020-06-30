package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(application: Application) : BaseViewModel(application) {

    fun updateDrinksLiveData(query: String) {
        drinkRepository.updateDrinksLiveData(query)
    }

    override fun getLiveData(): MutableLiveData<List<Drink>> {
        return drinkRepository.getLiveData()
    }

    override fun getCurrentData(): List<Drink> {
        return drinkRepository.getCurrentData()
    }
}