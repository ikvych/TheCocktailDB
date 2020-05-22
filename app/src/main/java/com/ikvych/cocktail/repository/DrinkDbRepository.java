package com.ikvych.cocktail.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.service.DrinkDao;
import com.ikvych.cocktail.service.DrinkDataBase;
import com.ikvych.cocktail.service.ImageLoadService;

import java.util.List;

public class DrinkDbRepository {

    private DrinkDao drinkDao;
    private Application application;
    private ImageLoadService imageLoadService;

    public DrinkDbRepository(Application application) {
        drinkDao = DrinkDataBase.getInstance(application).getDrinkDao();
        this.application = application;
        this.imageLoadService = new ImageLoadService();
    }

    public LiveData<List<Drink>> getDrinks() {
        return drinkDao.getAllDrink();
    }

    public LiveData<Drink> getDrinkById(Long idDrink) {
        return drinkDao.getDrinkById(idDrink);
    }

    public void insertDrink(Drink drink) {
        new InsertDrinkAsyncTask(drinkDao, application, imageLoadService).execute(drink);
    }

    private static class InsertDrinkAsyncTask extends AsyncTask<Drink, Void, Void> {

        private DrinkDao drinkDao;
        private Application application;
        private ImageLoadService imageLoadService;


        public InsertDrinkAsyncTask(DrinkDao drinkDao, Application application, ImageLoadService imageLoadService) {
            this.drinkDao = drinkDao;
            this.application = application;
            this.imageLoadService = imageLoadService;
        }

        @Override
        protected Void doInBackground(Drink... drinks) {
            synchronized (Thread.currentThread()) {
                imageLoadService.downloadImageAndSaveNewPath(drinks[0], application, Thread.currentThread());
                try {
                    Thread.currentThread().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            drinkDao.insert(drinks[0]);
            return null;
        }
    }
}
