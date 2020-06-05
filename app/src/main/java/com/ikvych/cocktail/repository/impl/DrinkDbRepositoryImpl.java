package com.ikvych.cocktail.repository.impl;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkDbRepository;
import com.ikvych.cocktail.service.DrinkDao;
import com.ikvych.cocktail.service.DrinkDataBase;

import java.util.List;

public class DrinkDbRepositoryImpl implements DrinkDbRepository {

    private DrinkDao drinkDao;

    public DrinkDbRepositoryImpl(Context context) {
        drinkDao = DrinkDataBase.getInstance(context).getDrinkDao();
    }

    @Override
    public LiveData<List<Drink>> getDrinks() {
        return drinkDao.getAllDrink();
    }


    @Override
    public void saveDrink(Drink drink) {
        new SaveDrinkAsyncTask(drinkDao).execute(drink);
    }

    private static class SaveDrinkAsyncTask extends AsyncTask<Drink, Void, Void> {

        private DrinkDao drinkDao;

        SaveDrinkAsyncTask(DrinkDao drinkDao) {
            this.drinkDao = drinkDao;
        }

        @Override
        protected Void doInBackground(Drink... drinks) {
            for (Drink drink : drinks) {
                drinkDao.insert(drink);
            }
            return null;
        }
    }
}
