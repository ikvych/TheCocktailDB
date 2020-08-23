package com.ikvych.cocktail.data.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.network.source.CocktailNetSource
import com.ikvych.cocktail.data.repository.impl.mapper.cocktail.CocktailRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.data.repository.model.cocktail.IngredientRepoModel
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel

class CocktailRepositoryImpl(
    private val dbSource: DrinkDbSource,
    private val netSource: CocktailNetSource,
    private val mapper: CocktailRepoModelMapper
) : BaseRepositoryImpl(), CocktailRepository {

    override val ingredientsListLiveData: LiveData<List<IngredientRepoModel>> =
        dbSource.ingredientsListLiveData
            .map { list ->
                list.map {
                    IngredientRepoModel(
                        it.ingredient
                    )
                }
            }

    override val cocktailsNetListLiveData: MutableLiveData<List<CocktailRepoModel>> =
        MutableLiveData()

    override suspend fun updateCocktailsLiveData(cocktailName: String) {
        cocktailsNetListLiveData.postValue(netSource.getCocktailsByName(cocktailName)
            .run {
                mapper.mapNetToRepoList(this ?: arrayListOf())
            })
    }

    override suspend fun getCocktailById(cocktailId: Long): CocktailRepoModel? {
        return netSource.getCocktailById(cocktailId)?.run(mapper::mapNetToRepo)
    }

    override fun findAllCocktailsLiveData(): LiveData<List<CocktailRepoModel>?> {
        return dbSource.findAllCocktailsLiveData()
            .map { list -> list?.map { mapper.mapDbToRepo(it) } }
    }

    override fun findCocktailByIdLiveData(cocktailId: Long): LiveData<CocktailRepoModel?> {
        return dbSource.findCocktailByIdLiveData(cocktailId).map { mapper.mapDbToRepo(it!!) }
    }

    override suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel) {
        dbSource.addOrReplaceCocktail(mapper.mapRepoToDb(cocktail))
    }

    override suspend fun findAllCocktails(): List<CocktailRepoModel>? {
        return dbSource.findAllCocktails()?.map { mapper.mapDbToRepo(it) }
    }

    override suspend fun findCocktailById(cocktailId: Long): CocktailRepoModel? {
        val localizedCocktailDbModel = dbSource.findCocktailById(cocktailId) ?: return null
        return mapper.mapDbToRepo(localizedCocktailDbModel)
    }

    override suspend fun findCocktailByDefaultName(defaultDrinkName: String): CocktailRepoModel? {
        val localizedCocktailDbModel =
            dbSource.findCocktailByDefaultName(defaultDrinkName) ?: return null
        return mapper.mapDbToRepo(localizedCocktailDbModel)
    }

    override suspend fun findCocktailOfTheDay(stringDate: String): CocktailRepoModel? {
        val cocktail = dbSource.findCocktailOfTheDay(stringDate) ?: return null
        return mapper.mapDbToRepo(cocktail)
    }

    override suspend fun removeCocktail(cocktail: CocktailRepoModel) {
        dbSource.removeCocktail(mapper.mapRepoToDb(cocktail))
    }

    override suspend fun findIngredient(ingredient: String): IngredientRepoModel {
        val ingredientDbModel = dbSource.findIngredient(ingredient)
        return IngredientRepoModel(
            ingredientDbModel.ingredient
        )
    }
}