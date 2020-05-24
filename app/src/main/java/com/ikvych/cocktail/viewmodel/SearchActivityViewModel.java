package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkApiRepository;

import java.util.Collections;
import java.util.List;

public class SearchActivityViewModel extends ViewModel {

    private DrinkApiRepository drinkApiRepository;
    private MutableLiveData<List<Drink>> drinks;

    public SearchActivityViewModel() {
        drinkApiRepository = new DrinkApiRepository();
        drinks = drinkApiRepository.getMutableLiveData();
    }

    public void getDrinksByNameData(String name) {
        drinkApiRepository.getMutableLiveData(name);
    }

    public MutableLiveData<List<Drink>> getDrinks() {
        return drinks;
    }

    public List<Drink> getCurrentData() {
        return drinkApiRepository.getDataOnRotation();
    }

}
