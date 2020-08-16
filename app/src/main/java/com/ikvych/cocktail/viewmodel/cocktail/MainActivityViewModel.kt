package com.ikvych.cocktail.viewmodel.cocktail

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.remoteconfig.ktx.get
import com.ikvych.cocktail.data.repository.source.CocktailRepository
import com.ikvych.cocktail.listener.ApplicationLifeCycleObserver
import com.ikvych.cocktail.presentation.mapper.cocktail.CocktailModelMapper
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.FirebaseHelper
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel(
    private val cocktailRepository: CocktailRepository,
    private val mapper: CocktailModelMapper,
    application: Application,
    savedStateHandle: SavedStateHandle,
    firebase: FirebaseHelper
) : CocktailViewModel(application, savedStateHandle, cocktailRepository, mapper, firebase),
    ApplicationLifeCycleObserver.OnLifecycleObserverListener {

    val cocktailOfTheDayLiveData: MutableLiveData<CocktailModel?> = MutableLiveData()
    private var lifecycleObserver: ApplicationLifeCycleObserver
    private var sharedPreferences: SharedPreferences = application.getSharedPreferences(
        MAIN_ACTIVITY_SHARED_PREFERENCE,
        Context.MODE_PRIVATE
    )
    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> =
        appSettingRepository.showNavigationBarTitleLiveData

    //визначає чи видимий switcher для показування видомості заголовку в BottomNavigationView
    val switcherVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData(true)

    val showBatteryStateLiveData: MutableLiveData<Boolean> =
        appSettingRepository.showBatteryStateLiveData

    init {
        lifecycleObserver = ApplicationLifeCycleObserver(this, sharedPreferences)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    fun checkForRemoteConfig() {
        firebase.fetchAndActivate { isChanged, remoteConfig ->
            // isChanged - визначає чи є зміни на сервері
            if (isChanged) {
                val titleVisibility = remoteConfig["main_toolbar_title_visibility"].asBoolean()
                val switcherVisibility = remoteConfig["switcher_toolbar_visibility"].asBoolean()
                appSettingRepository.showNavigationBarTitleLiveData.value = titleVisibility
                switcherVisibilityLiveData.value = switcherVisibility
            }
        }
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