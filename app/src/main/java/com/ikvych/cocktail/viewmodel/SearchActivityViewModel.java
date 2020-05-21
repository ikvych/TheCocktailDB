package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkApiRepository;

import java.util.List;

public class SearchActivityViewModel extends AndroidViewModel {

    private DrinkApiRepository drinkApiRepository;

    public SearchActivityViewModel(@NonNull Application application) {
        super(application);
        drinkApiRepository = new DrinkApiRepository();
    }

    public LiveData<List<Drink>> getDrinksByNameData(String name) {
        return drinkApiRepository.getMutableLiveData(name);
    }

}
