package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivityViewModel(
    application: Application
) : BaseViewModel(application) {

    val drinksLiveData: LiveData<List<Drink>> = drinkRepository.getDrinkDbLiveData()
    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllDrinksFromDb(): List<Drink> {
        val drinkApiService = drinksLiveData.value
        return drinkApiService ?: emptyList()
    }

    fun findDrinkByName(drinkName: String): Drink? {
        return drinkRepository.findDrinkByName(drinkName)
    }

    fun saveDrinkIntoDb(drink: Drink) {
        drinkRepository.saveDrinkIntoDb(drink)
    }

    fun getDrinkOfTheDay(): Drink? {
        val currentDate = Date()
        val pattern = "MM-dd-yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val stringDate: String = simpleDateFormat.format(currentDate)

        var drinkOfTheDay = drinkRepository.findDrinkOfTheDay(stringDate)
        if (drinkOfTheDay == null) {
            val allDrinks = drinksLiveData.value
            if (allDrinks.isNullOrEmpty()) {
                return null
            } else {
                val newDrinkOfTheDay: Drink = allDrinks.random()
                newDrinkOfTheDay.setDrinkOfDay(stringDate)
                drinkRepository.saveDrinkIntoDb(newDrinkOfTheDay)
            }
            drinkOfTheDay = drinkRepository.findDrinkOfTheDay(stringDate)
        }
        return drinkOfTheDay
    }
}