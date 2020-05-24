package com.ikvych.cocktail.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkApiRepository;

import java.util.Collections;
import java.util.List;

public class SearchActivityViewModel extends ViewModel {

    private DrinkApiRepository drinkApiRepository;

    public SearchActivityViewModel() {
        drinkApiRepository = new DrinkApiRepository();
    }

    public void updateDrinksLiveData(String searchQuery) {
        drinkApiRepository.updateDrinksLiveData(searchQuery);
    }

    public MutableLiveData<List<Drink>> getDrinksLiveData() {
        return drinkApiRepository.getDrinksLiveData();
    }

    public List<Drink> getCurrentData() {
        return drinkApiRepository.getCurrentData();
    }

}
