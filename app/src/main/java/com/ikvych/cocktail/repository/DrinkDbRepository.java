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
    private ImageLoadService imageLoadService;

    public DrinkDbRepository(Application application) {
        drinkDao = DrinkDataBase.getInstance(application).getDrinkDao();
        this.imageLoadService = new ImageLoadService(application);
    }

    public LiveData<List<Drink>> getDrinks() {
        return drinkDao.getAllDrink();
    }

    public LiveData<Drink> getDrinkById(Long idDrink) {
        return drinkDao.getDrinkById(idDrink);
    }

    public void saveDrinkIntoDbWithDownloadedPhoto(Drink drink) {
        new SaveDrinkAsyncTask(drinkDao, imageLoadService).execute(drink);
    }

    private static class SaveDrinkAsyncTask extends AsyncTask<Drink, Void, Void> {

        private DrinkDao drinkDao;
        private ImageLoadService imageLoadService;

        public SaveDrinkAsyncTask(DrinkDao drinkDao, ImageLoadService imageLoadService) {
            this.drinkDao = drinkDao;
            this.imageLoadService = imageLoadService;
        }

        @Override
        protected Void doInBackground(Drink... drinks) {
            synchronized (this) {
                imageLoadService.downloadImageAndSaveNewPath(drinks[0], this);
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            drinkDao.insert(drinks[0]);
            return null;
        }
    }
}
