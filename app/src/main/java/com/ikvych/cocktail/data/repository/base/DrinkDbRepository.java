package com.ikvych.cocktail.data.repository.base;

import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.data.entity.Drink;

import java.util.List;

public interface DrinkDbRepository {

    LiveData<List<Drink>> getDrinks();

    void saveDrink(Drink drink);
}
