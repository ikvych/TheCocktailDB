package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink


interface DrinkApiRepository {

    fun getLiveData(): MutableLiveData<List<Drink>>
    fun updateDrinksLiveData(query: String)
    fun getCurrentData(): List<Drink>
    fun initAllIngredient()
}