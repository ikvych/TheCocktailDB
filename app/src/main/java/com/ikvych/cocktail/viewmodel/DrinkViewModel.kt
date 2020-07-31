package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.ui.mapper.CocktailModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

open class DrinkViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper
) : BaseViewModel(
    application,
    savedStateHandle
) {

    fun saveFavoriteDrink(cocktail: CocktailModel) {
        cocktail.isFavorite = !cocktail.isFavorite
        launchRequest {
            cocktailRepository.addOrReplaceCocktail(mapper.mapFrom(cocktail))
        }
    }


    fun removeCocktail(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepository.removeCocktail(mapper.mapFrom(cocktail))
        }
    }

    fun findCocktailByName(cocktailName: String): MutableLiveData<CocktailModel?> {
        val cocktailLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()
        launchRequest(cocktailLiveData) {
            mapper.mapTo(cocktailRepository.findCocktailByDefaultName(cocktailName)!!)
        }
        return cocktailLiveData
    }

    fun findCocktailById(cocktailId: Long): MutableLiveData<CocktailModel?> {
        val cocktailLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()
        launchRequest(cocktailLiveData) {
            mapper.mapTo(cocktailRepository.findCocktailById(cocktailId)!!)
        }
        return cocktailLiveData
    }

}