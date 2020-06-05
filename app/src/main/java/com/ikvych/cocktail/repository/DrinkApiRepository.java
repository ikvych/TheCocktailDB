package com.ikvych.cocktail.repository;

import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.model.Drink;

import java.util.List;

public interface DrinkApiRepository {

    void updateDrinksLiveData(String name);

    MutableLiveData<List<Drink>> getDrinksLiveData();

    List<Drink> getCurrentData();
}
