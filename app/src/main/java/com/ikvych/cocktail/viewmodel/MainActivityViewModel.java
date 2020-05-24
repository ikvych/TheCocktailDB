package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkDbRepository;

import java.util.Collections;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private DrinkDbRepository drinkDbRepository;
    private LiveData<List<Drink>> drinks;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        drinkDbRepository = new DrinkDbRepository(application);
        drinks = drinkDbRepository.getDrinks();
    }

    public LiveData<List<Drink>> getDrinksLiveData() {
        return drinks;
    }

    public void saveDrink(Drink drink) {
        drinkDbRepository.saveDrink(drink);
    }

    public List<Drink> getCurrentData() {
        if (drinks.getValue() != null) {
            return drinks.getValue();
        }
        return Collections.emptyList();
    }




}
