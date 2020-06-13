package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkApiRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkApiRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(application: Application) : BaseViewModel(application) {
    private val apiRepository: DrinkApiRepository = DrinkApiRepositoryImpl()

    fun updateDrinksLiveData(query: String) {
        apiRepository.updateDrinksLiveData(query)
    }

    override fun getLiveData(): MutableLiveData<List<Drink>> {
        return apiRepository.getLiveData()
    }

    override fun getCurrentData(): List<Drink> {
        return apiRepository.getCurrentData()
    }
}