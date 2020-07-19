package com.ikvych.cocktail.data.db.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ikvych.cocktail.data.db.Table
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.model.entity.*

@Dao
interface CocktailDao : BaseDao {

    @Transaction
    fun addOrReplaceLocalizedCocktail(cocktail: LocalizedCocktailDbModel) {
        addOrReplaceCocktail(cocktail = cocktail.cocktailDbModel)
        addOrReplaceCocktailName(name = cocktail.localizedNameDbModel)
        addOrReplaceCocktailInstruction(instruction = cocktail.localizedInstructionDbModel)
        cocktail.ingredients.forEach {
            addOrReplaceCocktailIngredient(it)
            addOrReplaceCocktailIngredientCrossRef(CocktailIngredientCrossRef(cocktail.cocktailDbModel.id, it.ingredient))
        }
        cocktail.measures.forEach {
            addOrReplaceCocktailMeasure(it)
            addOrReplaceCocktailMeasureCrossRef(CocktailMeasureCrossRef(cocktail.cocktailDbModel.id, it.measure))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktail(cocktail: CocktailDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailName(name: LocalizedNameDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailInstruction(instruction: LocalizedInstructionDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailIngredient(ingredient: IngredientDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailMeasure(measure: MeasureDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailIngredientCrossRef(crossRef: CocktailIngredientCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrReplaceCocktailMeasureCrossRef(crossRef: CocktailMeasureCrossRef)

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