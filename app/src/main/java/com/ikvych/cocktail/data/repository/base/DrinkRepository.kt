package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient

interface DrinkRepository {

    fun getLiveData(): MutableLiveData<List<Drink>>
    fun updateDrinksLiveData(query: String)

    fun getCurrentData(): List<Drink>
    fun initAllIngredient()

    fun getJustDrinks(): List<Drink>

    fun getDrinks(): LiveData<List<Drink>>

    fun getFavoriteDrinks(): LiveData<List<Drink>>

    fun saveDrink(drink: Drink)

    fun getAllIngredient(): List<Ingredient>

    fun findDrinkById(drinkId: Long): Drink

    fun findDrinkByName(drinkName: String): Drink
}