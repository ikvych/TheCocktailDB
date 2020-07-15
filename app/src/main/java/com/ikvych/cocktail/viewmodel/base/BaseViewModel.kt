package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.dataTest.repository.AppSettingRepository
import com.ikvych.cocktail.dataTest.repository.impl.AppSettingRepositoryImpl
import com.ikvych.cocktail.ui.mapper.CocktailModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


open class BaseViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle,
    private val drinkRepository: CocktailRepository? = null,
    private val mapper: CocktailModelMapper? = null
) : AndroidViewModel(application) {

    protected val appSettingRepository: AppSettingRepository =
        AppSettingRepositoryImpl.instance(application)

    protected fun <T> launchRequest(
        liveData: LiveData<T>? = null,
        context: CoroutineContext = Dispatchers.IO,
        request: suspend CoroutineScope.() -> T
    ): Job {
        return viewModelScope.async {
            try {
                withContext(context) { request() }.apply { liveData?.setValue(this) }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    protected fun <T> LiveData<T>.setValue(value: T) {
        (this as? MutableLiveData)?.value = value
    }

    protected fun <T> LiveData<T>.postValue(value: T) {
        (this as? MutableLiveData)?.postValue(value)
    }

    val startCocktailDetailsLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()
    val selectedLanguageLiveData: MutableLiveData<Int> =
        appSettingRepository.selectedLanguageLiveData

    fun startNewDrinkDetails(cocktail: CocktailModel) {
        startCocktailDetailsLiveData.value = cocktail
    }

    fun saveFavoriteDrink(cocktail: CocktailModel) {
        cocktail.isFavorite = !cocktail.isFavorite
        launchRequest {
            drinkRepository?.addOrReplaceCocktail(mapper!!.mapFrom(cocktail))
        }
    }
}