package com.ikvych.cocktail.data.db.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikvych.cocktail.data.db.Table
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.network.model.Drink
import com.xtreeivi.cocktailsapp.data.db.model.CocktailDbModel

@Dao
interface CocktailDao : BaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktail(cocktail: CocktailDbModel)

    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE cocktail_of_day = :stringDate")
    fun findCocktailOfTheDay(stringDate: String) : CocktailDbModel?

    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE id = :cocktailId")
    fun findCocktailByIdLiveData(cocktailId: Long) : LiveData<CocktailDbModel?>

    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE id = :cocktailId")
    fun findCocktailById(cocktailId: Long) : CocktailDbModel?

    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE name_default = :defaultCocktailName")
    fun findCocktailByDefaultName(defaultCocktailName: String) : CocktailDbModel?

    @Query("SELECT * FROM ${Table.COCKTAIL}")
    fun findAllCocktails() : List<CocktailDbModel>?

    @Query("SELECT * FROM ${Table.COCKTAIL}")
    fun findAllCocktailsLiveData() : LiveData<List<CocktailDbModel>?>

    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE is_favorite = 1")
    fun findAllFavoriteCocktailsLiveData() : LiveData<List<CocktailDbModel>?>

}