package com.ikvych.cocktail.data.db.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.network.model.Drink
import com.xtreeivi.cocktailsapp.data.db.model.CocktailDbModel

interface DrinkDbSource : BaseDbSource{

    suspend fun addOrReplaceCocktail(cocktail: CocktailDbModel)
    suspend fun findCocktailOfTheDay(stringDate: String) : CocktailDbModel?
    fun findCocktailByIdLiveData(cocktailId: Long) : LiveData<CocktailDbModel?>
    suspend fun findCocktailById(cocktailId: Long) : CocktailDbModel?
    suspend fun findCocktailByDefaultName(cocktailDefaultName: String) : CocktailDbModel?
    suspend fun findAllCocktails() : List<CocktailDbModel>?
    fun findAllCocktailsLiveData() : LiveData<List<CocktailDbModel>?>

}