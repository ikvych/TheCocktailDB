package com.ikvych.cocktail.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.service.DrinkDao;
import com.ikvych.cocktail.service.DrinkDataBase;

import java.util.Collections;
import java.util.List;

public class DrinkDbRepository {

    private DrinkDao drinkDao;

    public DrinkDbRepository(Context context) {
        drinkDao = DrinkDataBase.getInstance(context).getDrinkDao();
    }

    public LiveData<List<Drink>> getDrinks() {
        return drinkDao.getAllDrink();
    }


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
