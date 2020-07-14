package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import androidx.lifecycle.*
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.repository.source.DrinkRepository
import com.ikvych.cocktail.dataTest.repository.AppSettingRepository
import com.ikvych.cocktail.dataTest.repository.impl.AppSettingRepositoryImpl
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


open class BaseViewModel(
    application: Application,
    val savedStateHandle: SavedStateHandle,
    private val drinkRepository1: DrinkRepository? = null
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

    val startDrinkDetailsLiveData: MutableLiveData<Drink?> = MutableLiveData()
    val selectedLanguageLiveData: MutableLiveData<Int> =
        appSettingRepository.selectedLanguageLiveData

    fun startNewDrinkDetails(drink: Drink) {
        startDrinkDetailsLiveData.value = drink
    }

    fun saveFavoriteDrink(drink: Drink) {
        if (drink.isFavorite()) {
            drink.setIsFavorite(false)
        } else {
            drink.setIsFavorite(true)
        }
        launchRequest {
            drinkRepository1?.saveDrinkIntoDb(drink)
        }
    }
}