package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository


open class BaseViewModel(application: Application): AndroidViewModel(application) {

    protected val drinkRepository: DrinkRepository = DrinkRepositoryImpl(application)
    protected lateinit var state: SavedStateHandle

}