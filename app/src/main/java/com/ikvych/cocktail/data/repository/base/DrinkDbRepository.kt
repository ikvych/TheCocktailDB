package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient


interface DrinkDbRepository {

    fun getDrinks(): LiveData<List<Drink>>

    fun getFavoriteDrinks(): LiveData<List<Drink>>

    fun saveDrink(drink: Drink)

    fun getAllIngredient(): List<Ingredient>

    fun findDrinkById(drinkId: Long): Drink

    fun findDrinkByName(drinkName: String): Drink
}