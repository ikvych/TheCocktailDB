package com.ikvych.cocktail.viewmodel

import android.app.Application
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.comparator.AlcoholDrinkComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.entity.Ingredient
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.CategoryDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.filter.type.IngredientDrinkFilter
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainActivityViewModel(
    application: Application
) : BaseViewModel(application) {

    val drinksLiveData: LiveData<List<Drink>> = drinkRepository.getDrinks()
    val navBarTitleVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllDrinksFromDb(): List<Drink> {
        val drinkApiService = drinksLiveData.value
        return drinkApiService ?: emptyList()
    }

    fun findDrinkByName(drinkName: String): Drink {
        return drinkRepository.findDrinkByName(drinkName)
    }

    fun saveDrinkIntoDb(drink: Drink) {
        drinkRepository.saveDrink(drink)
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
                drinkRepository.saveDrink(newDrinkOfTheDay)
            }
            drinkOfTheDay = drinkRepository.findDrinkOfTheDay(stringDate)
        }
        return drinkOfTheDay
    }
}