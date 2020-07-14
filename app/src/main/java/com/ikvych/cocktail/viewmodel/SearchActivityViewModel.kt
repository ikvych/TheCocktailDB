package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.data.network.DrinkApiRepositoryImpl
import com.ikvych.cocktail.data.network.DrinkApiRepository
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(
    private val drinkRepository: CocktailRepository,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    protected val drinkApiRepository: DrinkApiRepository = DrinkApiRepositoryImpl(application)

    val drinkLiveData: MutableLiveData<List<Drink>> =
        object : MediatorLiveData<List<Drink>>() {
            init {
                addSource(drinkApiRepository.getDrinkApiLiveData()) {
                    value = drinkApiRepository.getDrinkApiLiveData().value
                }
            }
        }

    fun updateDrinksLiveData(query: String) {
        drinkApiRepository.updateDrinkApiLiveData(query)
    }
}