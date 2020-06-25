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

    @Query("SELECT * FROM ingredient")
    fun getAllIngredients() : List<Ingredient>

    @Query("SELECT * FROM drink ORDER BY created DESC")
    fun getAllDrinks() : LiveData<List<Drink>>

    @Query("SELECT * FROM drink WHERE if_favorite ORDER BY created DESC")
    fun getAllFavoriteDrinks() : LiveData<List<Drink>>
}