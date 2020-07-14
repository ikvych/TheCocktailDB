package com.ikvych.cocktail.data.network

import androidx.lifecycle.MutableLiveData
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.data.repository.source.base.BaseRepository

interface DrinkApiRepository : BaseRepository {

    // Methods for work with Api
    fun getDrinkApiLiveData(): MutableLiveData<List<Drink>>
    fun updateDrinkApiLiveData(query: String)

}