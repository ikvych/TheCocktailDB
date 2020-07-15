package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.ui.mapper.CocktailModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class DrinkDetailViewModel(
    private val drinkRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle) {

    private val triggerObserver: Observer<in Any?> = Observer { }
    val cocktailLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()

    init {
        cocktailLiveData.observeForever(triggerObserver)
    }

    override fun onCleared() {
        cocktailLiveData.removeObserver(triggerObserver)
        super.onCleared()
    }

    fun findDrinkDbById(cocktailId: Long) {
        launchRequest(cocktailLiveData)  {
            mapper.mapTo(drinkRepository.findCocktailById(cocktailId)!!)
        }
    }

    fun saveDrinkIntoDb() {
        val currentCocktail = cocktailLiveData.value ?: return
        launchRequest {
            drinkRepository.addOrReplaceCocktail(mapper.mapFrom(currentCocktail))
        }
    }
}