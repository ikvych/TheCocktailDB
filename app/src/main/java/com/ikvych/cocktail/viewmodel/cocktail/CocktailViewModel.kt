package com.ikvych.cocktail.viewmodel.cocktail

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.FirebaseHelper
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

open class CocktailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    val firebase: FirebaseHelper
) : BaseViewModel(
    application,
    savedStateHandle
) {

    fun saveFavoriteDrink(cocktail: CocktailModel) {
        cocktail.isFavorite = !cocktail.isFavorite
        launchRequest {
            cocktailRepository.addOrReplaceCocktail(mapper.mapFrom(cocktail))
            // Винести з VIewModel
            if (cocktail.isFavorite) {
                firebase.logEvent(
                    ANALYTIC_EVENT_ADD_TO_FAVORITE, bundleOf(
                        ANALYTIC_KEY_COCKTAIL_ID to cocktail.id
                    )
                )
            } else {
                firebase.logEvent(
                    ANALYTIC_EVENT_REMOVE_FROM_FAVORITE, bundleOf(
                        ANALYTIC_KEY_COCKTAIL_ID to cocktail.id
                    )
                )
            }
        }
    }

    fun saveCocktail(cocktail: CocktailModel) {
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

    fun getCocktailById(cocktailId: Long): MutableLiveData<CocktailModel?> {
        val cocktailLiveData = MutableLiveData<CocktailModel?>()
        launchRequest(cocktailLiveData) {
           cocktailRepository.getCocktailById(cocktailId)?.run(mapper::mapTo)
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

    companion object {
        const val ANALYTIC_EVENT_ADD_TO_FAVORITE = "cocktail_favorite_add"
        const val ANALYTIC_EVENT_REMOVE_FROM_FAVORITE = "cocktail_favorite_remove"
        const val ANALYTIC_KEY_COCKTAIL_ID = "cocktail_id"
        const val ANALYTIC_KEY_USER_NAME = "user_name"
    }

}