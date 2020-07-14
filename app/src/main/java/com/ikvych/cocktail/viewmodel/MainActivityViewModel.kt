package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.data.repository.source.DrinkRepository
import com.ikvych.cocktail.listener.ApplicationLifeCycleObserver
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*

const val MAIN_ACTIVITY_SHARED_PREFERENCE = "MAIN_ACTIVITY_SHARED_PREFERENCE"

class MainActivityViewModel(
    private val drinkRepository: DrinkRepository,
    application: Application,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(application, savedStateHandle),
    ApplicationLifeCycleObserver.OnLifecycleObserverListener {

    val drinkOfTheDayLiveData: MutableLiveData<Drink?> = MutableLiveData()
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
        setDrinkOfTheDay()
    }

    fun findDrinkByName(drinkName: String): MutableLiveData<Drink?> {
        val drinkLiveData: MutableLiveData<Drink?> = MutableLiveData()
        launchRequest(drinkLiveData) {
            drinkRepository.findDrinkByName(drinkName)
        }
        return drinkLiveData
    }

    private fun setDrinkOfTheDay() {

        val currentDate = Date()
        val pattern = "MM-dd-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val stringDate: String = simpleDateFormat.format(currentDate)

        launchRequest(drinkOfTheDayLiveData) {
            val drinkOfTheDay: Drink? = drinkRepository.findDrinkOfTheDay(stringDate)
            if (drinkOfTheDay == null) {
                val allDrinks = drinkRepository.getAllDrinksFromDb()
                if (allDrinks.isNullOrEmpty()) {
                    return@launchRequest null
                } else {
                    val newDrinkOfTheDay: Drink = allDrinks.random()
                    newDrinkOfTheDay.setDrinkOfDay(stringDate)
                    drinkRepository.saveDrinkIntoDb(newDrinkOfTheDay)
                    drinkRepository.findDrinkByName(stringDate)
                }
            } else {
                drinkOfTheDay
            }
        }
    }
}