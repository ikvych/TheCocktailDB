package com.ikvych.cocktail.data.db.impl.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ikvych.cocktail.data.db.Table
import com.ikvych.cocktail.data.db.impl.dao.base.BaseDao
import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.model.entity.*

@Dao
interface CocktailDao : BaseDao {

    @get:Query("SELECT * FROM ${Table.INGREDIENT}")
    val ingredientsListLiveData: LiveData<List<IngredientDbModel>>

    @Transaction
    fun addOrReplaceLocalizedCocktail(cocktail: LocalizedCocktailDbModel) {
        addOrReplaceCocktail(cocktail = cocktail.cocktailDbModel)
        addOrReplaceCocktailName(name = cocktail.localizedNameDbModel)
        addOrReplaceCocktailInstruction(instruction = cocktail.localizedInstructionDbModel)
        cocktail.ingredientsWithMeasures.forEach {
            addOrReplaceCocktailIngredient(IngredientDbModel(it.ingredient))
            addOrReplaceCocktailMeasure(MeasureDbModel(it.measure))
            addOrReplaceIngredientsWithMeasures(it)
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
    fun addOrReplaceIngredientsWithMeasures(ingredientsWithMeasures: IngredientMeasureDbModel)

    @Delete
    fun removeCocktail(cocktail: CocktailDbModel)

    @Transaction
    fun findCocktailByDefaultName(defaultCocktailName: String) : LocalizedCocktailDbModel? {
        val localizedName = findLocalizedName(defaultCocktailName)
        return findCocktailById(localizedName.cocktailOwnerId)
    }

    @Query("SELECT * FROM ${Table.NAME} WHERE defaults_name = :name")
    fun findLocalizedName(name: String): LocalizedNameDbModel

    @Query("SELECT * FROM ${Table.INGREDIENT} WHERE ingredient = :ingredient")
    fun findIngredient(ingredient: String): IngredientDbModel

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
    fun findAllCocktails() : List<LocalizedCocktailDbModel>?

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL}")
    fun findAllCocktailsLiveData() : LiveData<List<LocalizedCocktailDbModel>?>

    @Transaction
    @Query("SELECT * FROM ${Table.COCKTAIL} WHERE is_favorite = 1")
    fun findAllFavoriteCocktailsLiveData() : LiveData<List<LocalizedCocktailDbModel>?>

}