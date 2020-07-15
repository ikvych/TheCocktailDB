package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.ui.mapper.CocktailModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    val cocktailLiveData: MutableLiveData<List<CocktailModel>> =
        object : MediatorLiveData<List<CocktailModel>>() {
            init {
                addSource(cocktailRepository.cocktailNetResponseLiveData) {
                    value = cocktailRepository.cocktailNetResponseLiveData.value?.map { mapper.mapTo(it) }
                }
            }
        }

    fun updateDrinksLiveData(query: String) {
        cocktailRepository.getCocktailsByName(query)
    }
}