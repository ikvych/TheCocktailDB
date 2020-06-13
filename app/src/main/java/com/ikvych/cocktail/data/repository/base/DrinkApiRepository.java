package com.ikvych.cocktail.data.repository.base;

import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.data.entity.Drink;

import java.util.List;

public interface DrinkApiRepository {

    void updateDrinksLiveData(String name);

    MutableLiveData<List<Drink>> getDrinksLiveData();

    List<Drink> getCurrentData();
}
