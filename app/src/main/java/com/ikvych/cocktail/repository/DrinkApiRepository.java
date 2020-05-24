package com.ikvych.cocktail.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.model.DrinkApiResponse;
import com.ikvych.cocktail.service.DrinkApiService;
import com.ikvych.cocktail.service.RetrofitInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkApiRepository {

    private MutableLiveData<List<Drink>> drinksLiveData;
    private DrinkApiService drinkApiService;

    public DrinkApiRepository() {
        drinksLiveData = new MutableLiveData<>();
        drinkApiService = RetrofitInstance.getService();
    }

    public void updateDrinksLiveData(String name) {
        Call<DrinkApiResponse> call = drinkApiService.getDrinksByName(name);

        call.enqueue(new Callback<DrinkApiResponse>() {
            @Override
            public void onResponse(Call<DrinkApiResponse> call, Response<DrinkApiResponse> response) {
                DrinkApiResponse drinkApiResponse = response.body();
                if (drinkApiResponse != null && drinkApiResponse.getDrinks() != null) {
                    drinksLiveData.setValue(drinkApiResponse.getDrinks());
                } else {
                    drinksLiveData.setValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<DrinkApiResponse> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Drink>> getDrinksLiveData() {
        return drinksLiveData;
    }

    public List<Drink> getCurrentData() {
        if (drinksLiveData.getValue() != null) {
            return drinksLiveData.getValue();
        }
        return Collections.emptyList();
    }

}
