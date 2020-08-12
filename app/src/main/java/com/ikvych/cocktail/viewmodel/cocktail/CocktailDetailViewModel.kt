package com.ikvych.cocktail.viewmodel.cocktail

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.FirebaseAnalyticHelper

class CocktailDetailViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle,
    analytic: FirebaseAnalyticHelper
) : CocktailViewModel(application, savedStateHandle, cocktailRepository, mapper, analytic) {

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