package com.ikvych.cocktail.data.db.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.model.cocktail.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.model.cocktail.entity.IngredientDbModel
import com.ikvych.cocktail.data.db.source.base.BaseDbSource

interface DrinkDbSource : BaseDbSource {

    val ingredientsListLiveData: LiveData<List<IngredientDbModel>>
    suspend fun addOrReplaceCocktail(cocktail: LocalizedCocktailDbModel)
    suspend fun findCocktailOfTheDay(stringDate: String) : LocalizedCocktailDbModel?
    fun findCocktailByIdLiveData(cocktailId: Long) : LiveData<LocalizedCocktailDbModel?>
    suspend fun findCocktailById(cocktailId: Long) : LocalizedCocktailDbModel?
    suspend fun findCocktailByDefaultName(cocktailDefaultName: String) : LocalizedCocktailDbModel?
    suspend fun findAllCocktails() : List<LocalizedCocktailDbModel>?
    fun findAllCocktailsLiveData() : LiveData<List<LocalizedCocktailDbModel>?>
    suspend fun removeCocktail(cocktail: LocalizedCocktailDbModel)
    suspend fun findIngredient(ingredient: String): IngredientDbModel

}