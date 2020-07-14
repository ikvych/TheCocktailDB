package com.ikvych.cocktail.data.repository.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.network.model.CocktailNetResponse
import com.ikvych.cocktail.data.network.model.DrinkApiResponse
import com.ikvych.cocktail.data.repository.source.base.BaseRepository
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel
import retrofit2.Call

interface CocktailRepository : BaseRepository{

    fun getCocktailsByName(cocktailName: String)

    fun findAllCocktailsLiveData(): LiveData<List<CocktailRepoModel>?>
    fun findCocktailByIdLiveData(cocktailId: Long): LiveData<CocktailRepoModel?>
    suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel)
    suspend fun findAllCocktails(): List<CocktailRepoModel>?
    suspend fun findCocktailById(cocktailId: Long): CocktailRepoModel?
    suspend fun findCocktailByDefaultName(defaultDrinkName: String): CocktailRepoModel?
    suspend fun findCocktailOfTheDay(stringDate: String): CocktailRepoModel?
}