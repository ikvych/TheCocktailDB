package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkApiRepository;

import java.util.Collections;
import java.util.List;

public class SearchActivityViewModel extends ActivityViewModel {

    private DrinkApiRepository drinkApiRepository;

    public SearchActivityViewModel(@NonNull Application application) {
        super(application);
        drinkApiRepository = new DrinkApiRepository();
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
