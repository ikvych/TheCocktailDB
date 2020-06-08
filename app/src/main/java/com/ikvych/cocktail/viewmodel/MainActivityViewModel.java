package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.data.repository.DrinkDbRepositoryImpl;
import com.ikvych.cocktail.data.repository.base.DrinkDbRepository;
import com.ikvych.cocktail.viewmodel.base.BaseViewModel;

import java.util.Collections;
import java.util.List;

public class MainActivityViewModel extends BaseViewModel {

    private DrinkDbRepository drinkDbRepository;
    private LiveData<List<Drink>> drinks;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        drinkDbRepository = new DrinkDbRepositoryImpl(application);
        drinks = drinkDbRepository.getDrinks();
    }

    @Override
    public LiveData<List<Drink>> getDrinksLiveData() {
        return drinks;
    }

    public void saveDrink(Drink drink) {
        drinkDbRepository.saveDrink(drink);
    }

    @Override
    public List<Drink> getCurrentData() {
        if (drinks.getValue() != null) {
            return drinks.getValue();
        }
        return Collections.emptyList();
    }
}
