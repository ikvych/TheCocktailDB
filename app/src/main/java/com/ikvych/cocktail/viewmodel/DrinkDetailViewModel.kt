package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.presentation.mapper.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel

class DrinkDetailViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : DrinkViewModel(application, savedStateHandle, cocktailRepository, mapper) {

    private val triggerObserver: Observer<in Any?> = Observer { }
    val cocktailLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()

    init {
        cocktailLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        cocktailLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }

    fun findCocktailDbById(cocktailId: Long) {
        launchRequest(cocktailLiveData)  {
            val result = cocktailRepository.findCocktailById(cocktailId)!!
            mapper.mapTo(result)
        }
    }

    fun saveCocktailIntoDb() {
        val currentCocktail = cocktailLiveData.value ?: return
        launchRequest {
            cocktailRepository.addOrReplaceCocktail(mapper.mapFrom(currentCocktail))
        }
    }
}