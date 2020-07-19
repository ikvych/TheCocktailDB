package com.ikvych.cocktail.data.db.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ikvych.cocktail.data.db.Table
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.data.db.model.entity.CocktailDbModel
import com.ikvych.cocktail.data.db.model.entity.LocalizedInstructionDbModel
import com.ikvych.cocktail.data.db.model.entity.LocalizedNameDbModel

@Dao
interface CocktailDao : BaseDao {

    @Transaction
    fun addOrReplaceLocalizedCocktail(cocktail: LocalizedCocktailDbModel) {
        addOrReplaceCocktail(cocktail = cocktail.cocktailDbModel)
        addOrReplaceCocktailName(name = cocktail.localizedNameDbModel)
        addOrReplaceCocktailInstruction(instruction = cocktail.localizedInstructionDbModel)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktail(cocktail: CocktailDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailName(name: LocalizedNameDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailInstruction(instruction: LocalizedInstructionDbModel)

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE cocktail_of_day = :stringDate")
    fun findCocktailOfTheDay(stringDate: String) : LocalizedCocktailDbModel?

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE id = :cocktailId")
    fun findCocktailByIdLiveData(cocktailId: Long) : LiveData<LocalizedCocktailDbModel?>

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE id = :cocktailId")
    fun findCocktailById(cocktailId: Long) : LocalizedCocktailDbModel?

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL}")
    fun findCocktailByDefaultName(/*defaultCocktailName: String*/) : LocalizedCocktailDbModel?

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL}")
    fun findAllLocalizedCocktail(): LiveData<List<LocalizedCocktailDbModel>>

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL}")
    fun findAllCocktails() : List<LocalizedCocktailDbModel>?

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL}")
    fun findAllCocktailsLiveData() : LiveData<List<LocalizedCocktailDbModel>?>

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE is_favorite = 1")
    fun findAllFavoriteCocktailsLiveData() : LiveData<List<LocalizedCocktailDbModel>?>

}