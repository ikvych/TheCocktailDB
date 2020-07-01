package com.ikvych.cocktail.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient

@Dao
interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drink: Drink)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM drink WHERE drink_of_day=:stringDate")
    fun findDrinkOfTheDay(stringDate: String) : Drink?

    @Query("SELECT * FROM drink WHERE id_drink=:drinkId")
    fun findDrinkById(drinkId: Long) : Drink

    @Query("SELECT * FROM drink WHERE str_drink=:drinkName")
    fun findDrinkByName(drinkName: String) : Drink

    @Query("SELECT * FROM ingredient ORDER BY str_ingredient ASC")
    fun getAllIngredients() : List<Ingredient>

    @Query("SELECT * FROM drink ORDER BY created DESC")
    fun getAllDrinks() : LiveData<List<Drink>>

}