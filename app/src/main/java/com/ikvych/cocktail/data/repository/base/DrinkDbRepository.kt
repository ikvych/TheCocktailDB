package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.entity.Drink


interface DrinkDbRepository {

    fun getDrinks(): LiveData<List<Drink>>

    fun saveDrink(drink: Drink)

}