package com.ikvych.cocktail.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.service.DrinkDao;
import com.ikvych.cocktail.service.DrinkDataBase;

import java.util.List;

public class DrinkDbRepository {

    private DrinkDao drinkDao;

    public DrinkDbRepository(Application application) {
        drinkDao = DrinkDataBase.getInstance(application).getDrinkDao();

    }

    public LiveData<List<Drink>> getDrinks() {
        return drinkDao.getAllDrink();
    }

    public LiveData<Drink> getDrinkById(Long idDrink) {
        return drinkDao.getDrinkById(idDrink);
    }

    public void insertDrink(Drink drink) {
        new InsertDrinkAsyncTask(drinkDao).execute(drink);
    }

    private static class InsertDrinkAsyncTask extends AsyncTask<Drink, Void, Void> {

        private DrinkDao drinkDao;

        public InsertDrinkAsyncTask(DrinkDao drinkDao) {
            this.drinkDao = drinkDao;
        }

        @Override
        protected Void doInBackground(Drink... drinks) {
            drinkDao.insert(drinks[0]);
            return null;
        }
    }
}
