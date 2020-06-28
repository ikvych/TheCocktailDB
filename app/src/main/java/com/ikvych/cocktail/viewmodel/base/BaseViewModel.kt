package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository


open class BaseViewModel(application: Application): AndroidViewModel(application) {

    protected val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application)

    open fun getCurrentData(): List<Drink> {
        return arrayListOf()
    }

    open fun getLiveData(): LiveData<List<Drink>>? {
        return null
    }

    open fun saveDrink(drink: Drink) {
        //stub
    }

    open fun getAllIngredient(): List<Ingredient> {
        return arrayListOf()
    }

}