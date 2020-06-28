package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class MainViewModel(
    application: Application
) : BaseViewModel(application) {

    private val drinksLiveData: LiveData<List<Drink>> = drinkRepository.getDrinks()
    private val favoriteDrinksLiveData: LiveData<List<Drink>> = drinkRepository.getFavoriteDrinks()
    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData()

    override fun getCurrentData(): List<Drink> {
        val drinkApiService = drinksLiveData.value
        return drinkApiService ?: emptyList()
    }

    fun getFavoriteCurrentData(): List<Drink> {
        return favoriteDrinksLiveData.value ?: emptyList()
    }

    fun findDrinkById(drinkId: Long): Drink {
        return drinkRepository.findDrinkById(drinkId)
    }

    fun findDrinkByName(drinkName: String): Drink {
        return drinkRepository.findDrinkByName(drinkName)
    }

    override fun saveDrink(drink: Drink) {
        drinkRepository.saveDrink(drink)
    }

    override fun getLiveData(): LiveData<List<Drink>> {
        return drinksLiveData
    }

    fun getFavoriteLiveData(): LiveData<List<Drink>> {
        return favoriteDrinksLiveData
    }

    override fun getAllIngredient(): List<Ingredient> {
        val ingredientList: List<Ingredient> = drinkRepository.getAllIngredient()
        if (ingredientList.isEmpty()) {
            drinkRepository.initAllIngredient()
        }
        return drinkRepository.getAllIngredient()
    }
}