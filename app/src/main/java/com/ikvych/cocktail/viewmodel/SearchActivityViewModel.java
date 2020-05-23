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

    public SearchActivityViewModel() {
        drinkApiRepository = new DrinkApiRepository();
    }

    public LiveData<List<Drink>> getDrinksByNameData(String name) {
        return drinkApiRepository.getMutableLiveData(name);
    }

    public List<Drink> getDataOnRotation() {
        return drinkApiRepository.getDataOnRotation();
    }

}
