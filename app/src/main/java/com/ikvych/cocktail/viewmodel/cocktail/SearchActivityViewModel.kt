package com.ikvych.cocktail.viewmodel.cocktail

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.presentation.extension.debounce
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.FirebaseHelper
import kotlinx.coroutines.Job

class SearchActivityViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle,
    analytic: FirebaseHelper
) : CocktailViewModel(application, savedStateHandle, cocktailRepository, mapper, analytic) {

    val cocktailLiveData: LiveData<List<CocktailModel>> =
        cocktailRepository.cocktailsNetListLiveData.map { mapper.mapTo(it) }

    private var searchJob: Job? = null

    private val searchObserver: Observer<String> = Observer {
        updateCocktailsLiveData(it)
    }

    //відслідковує чи було ініціалізоване value у drinkLiveData
    //Цю лайв дату відслідковує textView який повинний пропасти після того як було здійснено перший
    //запит на пошук дрінків
    val isCocktailLiveDataInitialized: LiveData<Boolean> = cocktailLiveData.map { true }

    val searchQueryLiveData: MutableLiveData<String> = MutableLiveData()
    private val shouldUpdateCocktailsLiveData: LiveData<String> = searchQueryLiveData.map {
        it
    }.debounce(1000)

    init {
        shouldUpdateCocktailsLiveData.observeForever(searchObserver)
    }

    private fun updateCocktailsLiveData(query: String) {
        if (searchJob?.isActive == true) searchJob?.cancel()
        searchJob = launchRequest {
            cocktailRepository.updateCocktailsLiveData(query)
        }
    }

    override fun onCleared() {
        shouldUpdateCocktailsLiveData.removeObserver(searchObserver)
        super.onCleared()
    }
}