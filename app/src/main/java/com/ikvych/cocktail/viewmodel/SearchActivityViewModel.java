package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkApiRepository;
import com.ikvych.cocktail.repository.impl.DrinkApiRepositoryImpl;

import java.util.List;

public class SearchActivityViewModel extends ActivityViewModel {

    private DrinkApiRepository drinkApiRepository;

    public SearchActivityViewModel(@NonNull Application application) {
        super(application);
        drinkApiRepository = new DrinkApiRepositoryImpl();
    }

    public void updateDrinksLiveData(String searchQuery) {
        drinkApiRepository.updateDrinksLiveData(searchQuery);
    }

    @Override
    public MutableLiveData<List<Drink>> getDrinksLiveData() {
        return drinkApiRepository.getDrinksLiveData();
    }

    @Override
    public List<Drink> getCurrentData() {
        return drinkApiRepository.getCurrentData();
    }

}
