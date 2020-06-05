package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;

import java.util.List;

public abstract class ActivityViewModel extends AndroidViewModel {

    ActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract List<Drink> getCurrentData();

    public abstract LiveData<List<Drink>> getDrinksLiveData();

}
