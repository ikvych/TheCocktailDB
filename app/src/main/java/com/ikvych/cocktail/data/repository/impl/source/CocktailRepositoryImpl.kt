package com.ikvych.cocktail.data.repository.impl.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.data.db.source.DrinkDbSource
import com.ikvych.cocktail.data.network.model.CocktailNetModel
import com.ikvych.cocktail.data.network.model.CocktailNetResponse
import com.ikvych.cocktail.data.network.model.DrinkApiResponse
import com.ikvych.cocktail.data.network.source.CocktailNetSource
import com.ikvych.cocktail.data.repository.impl.mapper.CocktailRepoModelMapper
import com.ikvych.cocktail.data.repository.impl.source.base.BaseRepositoryImpl
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.xtreeivi.cocktailsapp.data.db.model.CocktailDbModel
import com.xtreeivi.cocktailsapp.data.repository.model.CocktailRepoModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CocktailRepositoryImpl(
    private val dbSource: DrinkDbSource,
    private val netSource: CocktailNetSource,
    private val mapper: CocktailRepoModelMapper
) : BaseRepositoryImpl(), CocktailRepository {

    override val cocktailNetResponseLiveData: MutableLiveData<List<CocktailRepoModel>> = MutableLiveData()

    override fun getCocktailsByName(cocktailName: String) {
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
        return dbSource.findAllCocktailsLiveData().map{list -> list?.map{mapper.mapDbToRepo(it)}}
    }

    override fun findCocktailByIdLiveData(cocktailId: Long): LiveData<CocktailRepoModel?> {
        return dbSource.findCocktailByIdLiveData(cocktailId).map{mapper.mapDbToRepo(it!!)}
    }

    override suspend fun addOrReplaceCocktail(cocktail: CocktailRepoModel) {
        dbSource.addOrReplaceCocktail(mapper.mapRepoToDb(cocktail))
    }

    override suspend fun findAllCocktails(): List<CocktailRepoModel>? {
        return dbSource.findAllCocktails()?.map{mapper.mapDbToRepo(it)}
    }

    override suspend fun findCocktailById(cocktailId: Long): CocktailRepoModel? {
        return mapper.mapDbToRepo(dbSource.findCocktailById(cocktailId)!!)
    }

    override suspend fun findCocktailByDefaultName(defaultDrinkName: String): CocktailRepoModel? {
        return mapper.mapDbToRepo(dbSource.findCocktailByDefaultName(defaultDrinkName)!!)
    }

    override suspend fun findCocktailOfTheDay(stringDate: String): CocktailRepoModel? {
        return mapper.mapDbToRepo(dbSource.findCocktailOfTheDay(stringDate)!!)
    }
}