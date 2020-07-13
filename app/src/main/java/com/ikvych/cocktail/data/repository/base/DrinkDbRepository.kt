package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.entity.Drink


interface DrinkDbRepository {

    fun getDrinks(): LiveData<List<Drink>>

    fun getFavoriteDrinks(): LiveData<List<Drink>>

    fun saveDrink(drink: Drink)

    fun removeDrink(drink: Drink)

    fun findDrinkById(drinkId: Long): Drink

    fun findDrinkByName(drinkName: String): Drink
}