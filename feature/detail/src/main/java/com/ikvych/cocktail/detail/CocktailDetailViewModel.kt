package com.ikvych.cocktail.detail

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.prresentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.prresentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.prresentation.util.FirebaseHelper
import com.ikvych.cocktail.prresentation.viewmodel.cocktail.CocktailViewModel
import com.ikvych.cocktail.repository.source.CocktailRepository

class CocktailDetailViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle,
    analytic: FirebaseHelper
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