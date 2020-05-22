package com.ikvych.cocktail.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkDbRepository;
import com.ikvych.cocktail.service.ImageLoadService;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private DrinkDbRepository drinkDbRepository;
    private ImageLoadService imageLoadRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        drinkDbRepository = new DrinkDbRepository(application);
        imageLoadRepository = new ImageLoadService();
    }

    public LiveData<List<Drink>> getAllDrinksFromDb() {
        return drinkDbRepository.getDrinks();
    }

    public LiveData<Drink> getDrinkById(Long idDrink) {
        return drinkDbRepository.getDrinkById(idDrink);
    }

    public void insertDrinkIntoDB(Drink drink) {
        drinkDbRepository.insertDrink(drink);
    }

/*    public void downloadImageAndSaveNewPath(Drink drink, Context context) {
        imageLoadRepository.downloadImageAndSaveNewPath(drink, context);
    }*/
}
