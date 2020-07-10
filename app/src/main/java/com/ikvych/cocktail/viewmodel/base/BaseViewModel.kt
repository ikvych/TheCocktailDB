package com.ikvych.cocktail.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ikvych.cocktail.data.entity.Drink


abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    abstract fun getCurrentData(): List<Drink>

    abstract fun getLiveData(): LiveData<List<Drink>>

    open fun saveDrink(drink: Drink) {
        //stub
    }

}