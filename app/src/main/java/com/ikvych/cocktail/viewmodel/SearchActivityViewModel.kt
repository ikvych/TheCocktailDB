package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.ui.mapper.CocktailModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel


class SearchActivityViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : DrinkViewModel(application, savedStateHandle, cocktailRepository, mapper) {

    val cocktailLiveData: LiveData<List<CocktailModel>> =
        cocktailRepository.cocktailNetResponseLiveData.map{mapper.mapTo(it)}

    //відслідковує чи було ініціалізоване value у drinkLiveData
    //Цю лайв дату відслідковує textView який повинний пропасти після того як було здійснено перший
    //запит на пошук дрінків
    val isCocktailLiveDataInitialized: LiveData<Boolean> = cocktailLiveData.map { true }

    fun updateCocktailsLiveData(query: String) {
        cocktailRepository.updateCocktailsLiveData(query)
    }
}