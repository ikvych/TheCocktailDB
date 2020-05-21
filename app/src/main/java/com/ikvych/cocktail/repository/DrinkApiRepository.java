package com.ikvych.cocktail.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.model.DrinkApiResponse;
import com.ikvych.cocktail.service.DrinkApiService;
import com.ikvych.cocktail.service.RetrofitInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkApiRepository {

    private List<Drink> drinkList = new ArrayList<>();
    private MutableLiveData<List<Drink>> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Drink>> getMutableLiveData(String name) {
        DrinkApiService drinkApiService = RetrofitInstance.getService();

        Call<DrinkApiResponse> call = drinkApiService.getDrinksByName(name);

        call.enqueue(new Callback<DrinkApiResponse>() {
            @Override
            public void onResponse(Call<DrinkApiResponse> call, Response<DrinkApiResponse> response) {
                DrinkApiResponse drinkApiResponse = response.body();
                if (drinkApiResponse != null && drinkApiResponse.getDrinks() != null) {
                    drinkList = drinkApiResponse.getDrinks();
                    mutableLiveData.setValue(drinkList);
                }
            }

            @Override
            public void onFailure(Call<DrinkApiResponse> call, Throwable t) {

            }
        });

        return mutableLiveData;
    }
}
