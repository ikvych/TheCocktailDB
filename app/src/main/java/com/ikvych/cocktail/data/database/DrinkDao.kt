package com.ikvych.cocktail.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ikvych.cocktail.data.entity.Drink

@Dao
interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drink: Drink)

    @Delete
    fun delete(drink: Drink)

    @Query("SELECT * FROM drink WHERE id_drink=:drinkId")
    fun findDrinkById(drinkId: Long) : Drink

    @Query("SELECT * FROM drink WHERE str_drink=:drinkName")
    fun findDrinkByName(drinkName: String) : Drink

    @Query("SELECT * FROM drink ORDER BY created DESC")
    fun getAllDrinks() : LiveData<List<Drink>>

    @Query("SELECT * FROM drink WHERE if_favorite ORDER BY created DESC")
    fun getAllFavoriteDrinks() : LiveData<List<Drink>>
}