package com.ikvych.cocktail.viewmodel.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.data.entity.Drink;

import java.util.List;

public abstract class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract List<Drink> getCurrentData();

    public abstract LiveData<List<Drink>> getDrinksLiveData();

}
