package com.ikvych.cocktail.repository;

import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;

import java.util.List;

public interface DrinkDbRepository {

    LiveData<List<Drink>> getDrinks();

    void saveDrink(Drink drink);
}
