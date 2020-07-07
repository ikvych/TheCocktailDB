package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import com.ikvych.cocktail.dataTest.repository.AppSettingRepository
import com.ikvych.cocktail.dataTest.repository.impl.AppSettingRepositoryImpl


open class BaseViewModel(application: Application): AndroidViewModel(application) {

    protected val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application)
    protected val appSettingRepository: AppSettingRepository = AppSettingRepositoryImpl.instance(application)
    protected lateinit var state: SavedStateHandle

    val startDrinkDetailsLiveData: MutableLiveData<Drink?> = MutableLiveData<Drink?>()

    fun startNewDrinkDetails(drink: Drink) {
        startDrinkDetailsLiveData.value = drink
    }

    fun saveFavoriteDrink(drink: Drink) {
        if (drink.isFavorite()) {
            drink.setIsFavorite(false)
        } else {
            drink.setIsFavorite(true)
        }
        drinkRepository.saveDrinkIntoDb(drink)
    }

}