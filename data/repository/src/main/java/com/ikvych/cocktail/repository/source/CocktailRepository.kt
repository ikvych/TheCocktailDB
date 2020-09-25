package com.ikvych.cocktail.repository.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.repository.model.cocktail.IngredientRepoModel
import com.ikvych.cocktail.repository.source.base.BaseRepository
import com.ikvych.cocktail.repository.model.cocktail.CocktailRepoModel

interface CocktailRepository : BaseRepository {

    val ingredientsListLiveData: LiveData<List<IngredientRepoModel>>
    val cocktailsNetListLiveData: MutableLiveData<List<CocktailRepoModel>>
    suspend fun updateCocktailsLiveData(cocktailName: String)
    suspend fun getCocktailById(cocktailId: Long): CocktailRepoModel?

    fun findAllCocktailsLiveData(): LiveData<List<CocktailRepoModel>?>
    fun findCocktailByIdLiveData(cocktailId: Long): LiveData<CocktailRepoModel?>
    suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel)
    suspend fun findAllCocktails(): List<CocktailRepoModel>?
    suspend fun findCocktailById(cocktailId: Long): CocktailRepoModel?
    suspend fun findCocktailByDefaultName(defaultDrinkName: String): CocktailRepoModel?
    suspend fun findCocktailOfTheDay(stringDate: String): CocktailRepoModel?
    suspend fun removeCocktail(cocktail: CocktailRepoModel)
    suspend fun findIngredient(ingredient: String): IngredientRepoModel
}