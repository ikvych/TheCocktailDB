package com.ikvych.cocktail.data.repository.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.db.model.Drink

interface DrinkRepository : BaseRepository{

    // Methods for work with Api
    fun getDrinkApiLiveData(): MutableLiveData<List<Drink>>

    fun updateDrinkApiLiveData(query: String)

}