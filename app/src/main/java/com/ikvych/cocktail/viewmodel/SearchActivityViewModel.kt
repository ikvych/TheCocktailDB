package com.ikvych.cocktail.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.data.repository.DrinkRepositoryImpl
import com.ikvych.cocktail.data.repository.base.DrinkRepository
import com.ikvych.cocktail.viewmodel.base.BaseViewModel


class SearchActivityViewModel(application: Application) : DrinkViewModel(application) {

    val drinkLiveData: MutableLiveData<List<Drink>> = drinkRepository.getDrinkApiLiveData()
    //відслідковує чи було ініціалізоване value у drinkLiveData
    //Цю лайв дату відслідковує textView який повинний пропасти після того як було здійснено перший
    //запит на пошук дрінків
    val isDrinkLiveDataInitialized: LiveData<Boolean> = drinkLiveData.map { true }

    fun updateDrinksLiveData(query: String) {
        drinkRepository.updateDrinkApiLiveData(query)
    }
}