package com.ikvych.cocktail.data.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ikvych.cocktail.data.db.model.entity.IngredientDbModel
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.network.model.CocktailNetResponse
import com.ikvych.cocktail.data.network.source.CocktailNetSource
import com.ikvych.cocktail.data.repository.impl.mapper.CocktailRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.data.repository.model.IngredientRepoModel
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CocktailRepositoryImpl(
    private val dbSource: DrinkDbSource,
    private val netSource: CocktailNetSource,
    private val mapper: CocktailRepoModelMapper
) : BaseRepositoryImpl(), CocktailRepository {

    override val ingredientsListLiveData: LiveData<List<IngredientRepoModel>> =
        dbSource.ingredientsListLiveData
            .map {
                list -> list.map { IngredientRepoModel(it.ingredient) }
            }

    override val cocktailNetResponseLiveData: MutableLiveData<List<CocktailRepoModel>> =
        MutableLiveData()

    override fun updateCocktailsLiveData(cocktailName: String) {
        val call: Call<CocktailNetResponse?> = netSource.getCocktailsByName(cocktailName)

        call.enqueue(object : Callback<CocktailNetResponse?> {

            override fun onFailure(call: Call<CocktailNetResponse?>, t: Throwable) {
                println("Can't get drinks list from Api")
            }

            override fun onResponse(
                call: Call<CocktailNetResponse?>,
                response: Response<CocktailNetResponse?>
            ) {
                val cocktailNetResponse = response.body()
                if (cocktailNetResponse?.cocktails != null) {
                    cocktailNetResponseLiveData.setValue(mapper.mapNetToRepoList(cocktailNetResponse.cocktails))
                } else {
                    cocktailNetResponseLiveData.setValue(emptyList())
                }
            }

        })
    }

    override fun findAllCocktailsLiveData(): LiveData<List<CocktailRepoModel>?> {
        return dbSource.findAllCocktailsLiveData()
            .map { list -> list?.map { mapper.mapDbToRepo(it) } }
    }

    override fun findCocktailByIdLiveData(cocktailId: Long): LiveData<CocktailRepoModel?> {
        return dbSource.findCocktailByIdLiveData(cocktailId).map { mapper.mapDbToRepo(it!!) }
    }

    override suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel) {
        val result = mapper.mapRepoToDb(cocktail)
        dbSource.addOrReplaceCocktail(result)
    }

    override suspend fun findAllCocktails(): List<CocktailRepoModel>? {
        return dbSource.findAllCocktails()?.map { mapper.mapDbToRepo(it) }
    }

    override suspend fun findCocktailById(cocktailId: Long): CocktailRepoModel? {
        val result = dbSource.findCocktailById(cocktailId)!!
        return mapper.mapDbToRepo(result)
    }

    override suspend fun findCocktailByDefaultName(defaultDrinkName: String): CocktailRepoModel? {
        return mapper.mapDbToRepo(dbSource.findCocktailByDefaultName(defaultDrinkName)!!)
    }

    override suspend fun findCocktailOfTheDay(stringDate: String): CocktailRepoModel? {
        return mapper.mapDbToRepo(dbSource.findCocktailOfTheDay(stringDate)!!)
    }

    override suspend fun removeCocktail(cocktail: CocktailRepoModel) {
        dbSource.removeCocktail(mapper.mapRepoToDb(cocktail))
    }

    override suspend fun findIngredient(ingredient: String): IngredientRepoModel {
        val result = dbSource.findIngredient(ingredient)
        return IngredientRepoModel(
            result.ingredient
        )
    }
}