package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.listener.ApplicationLifeCycleObserver
import com.ikvych.cocktail.ui.mapper.CocktailModelMapper
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*

const val MAIN_ACTIVITY_SHARED_PREFERENCE = "MAIN_ACTIVITY_SHARED_PREFERENCE"

class MainActivityViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle),
    ApplicationLifeCycleObserver.OnLifecycleObserverListener {

    val cocktailOfTheDayLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()
    private var lifecycleObserver: ApplicationLifeCycleObserver
    private var sharedPreferences: SharedPreferences = application.getSharedPreferences(
        MAIN_ACTIVITY_SHARED_PREFERENCE,
        Context.MODE_PRIVATE
    )
    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> =
        appSettingRepository.showNavigationBarTitleLiveData
    val showBatteryStateLiveData: MutableLiveData<Boolean> =
        appSettingRepository.showBatteryStateLiveData

    init {
        lifecycleObserver = ApplicationLifeCycleObserver(this, sharedPreferences)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    override fun onCleared() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(lifecycleObserver)
        super.onCleared()
    }

    override fun shouldShowDrinkOfTheDay() {
        setCocktailOfTheDay()
    }

    fun removeCocktail(cocktail: CocktailModel) {
        launchRequest {
            cocktailRepository.removeCocktail(mapper.mapFrom(cocktail))
        }
    }

    fun findCocktailByName(cocktailName: String): MutableLiveData<CocktailModel?> {
        val cocktailLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()
        launchRequest(cocktailLiveData) {
            mapper.mapTo(cocktailRepository.findCocktailByDefaultName(cocktailName)!!)
        }
        return cocktailLiveData
    }

    private fun setCocktailOfTheDay() {

        val currentDate = Date()
        val pattern = "MM-dd-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val stringDate: String = simpleDateFormat.format(currentDate)

        launchRequest(cocktailOfTheDayLiveData) {
            val cocktailOfTheDay = cocktailRepository.findCocktailOfTheDay(stringDate)
            if (cocktailOfTheDay == null) {
                val allDrinks = cocktailRepository.findAllCocktails()
                if (allDrinks.isNullOrEmpty()) {
                    return@launchRequest null
                } else {
                    val newDrinkOfTheDay = allDrinks.random()
                    newDrinkOfTheDay.cocktailOfTheDay = stringDate
                    cocktailRepository.addOrReplaceCocktail(newDrinkOfTheDay)
                    mapper.mapTo(cocktailRepository.findCocktailOfTheDay(stringDate)!!)
                }
            } else {
                mapper.mapTo(cocktailOfTheDay)
            }
        }
    }
}