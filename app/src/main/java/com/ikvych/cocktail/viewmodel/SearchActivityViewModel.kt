package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.presentation.extension.debounce
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import kotlinx.coroutines.Job


class SearchActivityViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : DrinkViewModel(application, savedStateHandle, cocktailRepository, mapper) {

    val cocktailLiveData: LiveData<List<CocktailModel>> =
        cocktailRepository.cocktailsNetListLiveData.map { mapper.mapTo(it) }

    private var searchJob: Job? = null

    val searchObserver: Observer<String> = Observer {
        if (connectivityInfoLiveData.value == true) {
            updateCocktailsLiveData(it)
        } else {
            Toast.makeText(
                this.getApplication<Application>(),
                this.getApplication<Application>().getString(R.string.app_no_internet_connection),
                Toast.LENGTH_LONG
            ).show()
        }
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