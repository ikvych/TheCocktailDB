package com.ikvych.cocktail.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.data.database.DrinkDao;
import com.ikvych.cocktail.data.database.DrinkDataBase;
import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.data.repository.base.DrinkDbRepository;

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
