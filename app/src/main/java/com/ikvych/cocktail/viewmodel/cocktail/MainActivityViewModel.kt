package com.ikvych.cocktail.viewmodel.cocktail

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.listener.ApplicationLifeCycleObserver
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.FirebaseAnalyticHelper
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle,
    analytic: FirebaseAnalyticHelper
) : CocktailViewModel(application, savedStateHandle, cocktailRepository, mapper, analytic),
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
        showDrinkOfTheDay()
    }

    private fun showDrinkOfTheDay() {

        val currentDate = Date()
        val pattern = "MM-dd-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val stringDate: String = simpleDateFormat.format(currentDate)

        launchRequest(cocktailOfTheDayLiveData) {
            val repo = cocktailRepository
            val cocktailOfTheDay = repo.findCocktailOfTheDay(stringDate)
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

    companion object {
        const val MAIN_ACTIVITY_SHARED_PREFERENCE = "MAIN_ACTIVITY_SHARED_PREFERENCE"
    }
}