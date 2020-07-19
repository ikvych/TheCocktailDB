package com.ikvych.cocktail.data.db.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.model.entity.CocktailDbModel

interface DrinkDbSource : BaseDbSource{

    suspend fun addOrReplaceCocktail(cocktail: LocalizedCocktailDbModel)
    suspend fun findCocktailOfTheDay(stringDate: String) : LocalizedCocktailDbModel?
    fun findCocktailByIdLiveData(cocktailId: Long) : LiveData<LocalizedCocktailDbModel?>
    suspend fun findCocktailById(cocktailId: Long) : LocalizedCocktailDbModel?
    suspend fun findCocktailByDefaultName(cocktailDefaultName: String) : LocalizedCocktailDbModel?
    suspend fun findAllCocktails() : List<LocalizedCocktailDbModel>?
    fun findAllCocktailsLiveData() : LiveData<List<LocalizedCocktailDbModel>?>

}