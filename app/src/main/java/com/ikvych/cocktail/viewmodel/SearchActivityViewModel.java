package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.data.repository.DrinkApiRepositoryImpl;
import com.ikvych.cocktail.data.repository.base.DrinkApiRepository;
import com.ikvych.cocktail.viewmodel.base.BaseViewModel;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class SearchActivityViewModel extends BaseViewModel {

    private DrinkApiRepository drinkApiRepository;

    public SearchActivityViewModel(@NonNull Application application) {
        super(application);
        drinkApiRepository = new DrinkApiRepositoryImpl();
    }

    public void updateDrinksLiveData(String searchQuery) {
        drinkApiRepository.updateDrinksLiveData(searchQuery);
    }

    @SuppressWarnings("unused")
    @Override
    public MutableLiveData<List<Drink>> getDrinksLiveData() {
        return drinkApiRepository.getDrinksLiveData();
    }

    @Override
    public List<Drink> getCurrentData() {
        return drinkApiRepository.getCurrentData();
    }

}
