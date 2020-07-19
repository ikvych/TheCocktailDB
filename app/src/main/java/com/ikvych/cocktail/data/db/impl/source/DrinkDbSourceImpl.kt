package com.ikvych.cocktail.data.db.impl.source

import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.db.impl.dao.CocktailDao
import com.ikvych.cocktail.data.db.model.LocalizedCocktailDbModel
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.db.model.entity.CocktailDbModel

class DrinkDbSourceImpl(
    private val drinkDao: CocktailDao
) : DrinkDbSource {

    override suspend fun addOrReplaceCocktail(cocktail: LocalizedCocktailDbModel) {
        drinkDao.addOrReplaceLocalizedCocktail(cocktail)
    }

    override suspend fun findCocktailOfTheDay(stringDate: String) : LocalizedCocktailDbModel? {
        return drinkDao.findCocktailOfTheDay(stringDate)
    }

    override fun findCocktailByIdLiveData(cocktailId: Long) : LiveData<LocalizedCocktailDbModel?> {
        return drinkDao.findCocktailByIdLiveData(cocktailId)
    }

    override suspend fun findCocktailById(cocktailId: Long) : LocalizedCocktailDbModel? {
        return drinkDao.findCocktailById(cocktailId)
    }

    override suspend fun findCocktailByDefaultName(cocktailDefaultName: String) : LocalizedCocktailDbModel? {
/*        val localizedName = drinkDao.findLocalizedName(cocktailDefaultName)
        drinkDao.findCocktailById(localizedName.cocktailOwnerId)*/
        return drinkDao.findCocktailByDefaultName(cocktailDefaultName)
    }

    override suspend fun findAllCocktails() : List<LocalizedCocktailDbModel>? {
        return drinkDao.findAllCocktails()
    }

    override fun findAllCocktailsLiveData() : LiveData<List<LocalizedCocktailDbModel>?> {
        return drinkDao.findAllCocktailsLiveData()
    }

    override suspend fun removeCocktail(cocktail: LocalizedCocktailDbModel) {
        drinkDao.removeCocktail(cocktail.cocktailDbModel)
    }
}