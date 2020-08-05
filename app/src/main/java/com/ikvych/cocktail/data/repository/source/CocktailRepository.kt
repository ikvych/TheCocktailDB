package com.ikvych.cocktail.data.repository.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.repository.model.cocktail.IngredientRepoModel
import com.ikvych.cocktail.data.repository.source.base.BaseRepository
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel

interface CocktailRepository : BaseRepository{

    val ingredientsListLiveData: LiveData<List<IngredientRepoModel>>
    val cocktailsNetListLiveData: MutableLiveData<List<CocktailRepoModel>>
    suspend fun updateCocktailsLiveData(cocktailName: String)

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