package com.ikvych.cocktail.data.db.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.model.Drink

@Dao
interface DrinkDao : BaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drink: Drink)

    @Query("SELECT * FROM drink WHERE drink_of_day=:stringDate")
    fun findDrinkOfTheDay(stringDate: String) : Drink?

    @Query("SELECT * FROM drink WHERE id_drink=:drinkId")
    fun findDrinkLiveDataById(drinkId: Long) : LiveData<Drink?>

    @Query("SELECT * FROM drink WHERE id_drink=:drinkId")
    suspend fun findDrinkById(drinkId: Long) : Drink

    @Query("SELECT * FROM drink WHERE str_drink=:drinkName")
    fun findDrinkByName(drinkName: String) : Drink

    @Query("SELECT * FROM drink")
    fun getAllDrinks() : List<Drink>

    @Query("SELECT * FROM drink ORDER BY created DESC")
    fun getAllDrinksLiveData() : LiveData<List<Drink>>

    @Query("SELECT * FROM drink WHERE is_favorite = 0 ORDER BY created DESC")
    fun getAllFavoriteDrinksLiveData() : LiveData<List<Drink>>

}