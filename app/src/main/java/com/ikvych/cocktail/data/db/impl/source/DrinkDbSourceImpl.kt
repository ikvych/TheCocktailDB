package com.ikvych.cocktail.data.db.impl.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.impl.dao.CocktailDao
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.xtreeivi.cocktailsapp.data.db.model.CocktailDbModel

class DrinkDbSourceImpl(
    private val drinkDao: CocktailDao
) : DrinkDbSource {

    override suspend fun addOrReplaceCocktail(cocktail: CocktailDbModel) {
        drinkDao.addOrReplaceCocktail(cocktail)
    }

    override suspend fun findCocktailOfTheDay(stringDate: String) : CocktailDbModel? {
        return drinkDao.findCocktailOfTheDay(stringDate)
    }

    override fun findCocktailByIdLiveData(cocktailId: Long) : LiveData<CocktailDbModel?> {
        return drinkDao.findCocktailByIdLiveData(cocktailId)
    }

    override suspend fun findCocktailById(cocktailId: Long) : CocktailDbModel? {
        return drinkDao.findCocktailById(cocktailId)
    }

    override suspend fun findCocktailByDefaultName(cocktailDefaultName: String) : CocktailDbModel? {
        return drinkDao.findCocktailByDefaultName(cocktailDefaultName)
    }

    override suspend fun findAllCocktails() : List<CocktailDbModel>? {
        return drinkDao.findAllCocktails()
    }

    override fun findAllCocktailsLiveData() : LiveData<List<CocktailDbModel>?> {
        return drinkDao.findAllCocktailsLiveData()
    }
}