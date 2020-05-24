package com.ikvych.cocktail.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkApiRepository;

import java.util.List;

public class SearchActivityViewModel extends ViewModel {

    private DrinkApiRepository drinkApiRepository;
    private MutableLiveData<List<Drink>> drinks;

    public SearchActivityViewModel() {
        drinkApiRepository = new DrinkApiRepository();
        drinks = drinkApiRepository.updateLiveData();
    }

    public void findDrinksByName(String name) {
        drinkApiRepository.updateLiveData(name);
    }

    public MutableLiveData<List<Drink>> getDrinks() {
        return drinks;
    }

    public List<Drink> getCurrentData() {
        return drinkApiRepository.getDataOnRotation();
    }

}
