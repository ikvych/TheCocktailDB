package com.ikvych.cocktail.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.data.entity.DrinkApiResponse;
import com.ikvych.cocktail.data.network.DrinkApiService;
import com.ikvych.cocktail.data.network.RetrofitInstance;
import com.ikvych.cocktail.data.repository.base.DrinkApiRepository;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

@SuppressWarnings("CanBeFinal")
public class DrinkApiRepositoryImpl implements DrinkApiRepository {

    private MutableLiveData<List<Drink>> drinksLiveData;
    private DrinkApiService drinkApiService;

    public DrinkApiRepositoryImpl() {
        drinksLiveData = new MutableLiveData<>();
        drinkApiService = RetrofitInstance.getService();
    }

    @Override
    public void updateDrinksLiveData(String name) {
        Call<DrinkApiResponse> call = drinkApiService.getDrinksByName(name);

        call.enqueue(new Callback<DrinkApiResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<DrinkApiResponse> call, Response<DrinkApiResponse> response) {
                DrinkApiResponse drinkApiResponse = response.body();
                if (drinkApiResponse != null && drinkApiResponse.getDrinks() != null) {
                    drinksLiveData.setValue(drinkApiResponse.getDrinks());
                } else {
                    drinksLiveData.setValue(Collections.emptyList());
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<DrinkApiResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public MutableLiveData<List<Drink>> getDrinksLiveData() {
        return drinksLiveData;
    }

    @Override
    public List<Drink> getCurrentData() {
        if (drinksLiveData.getValue() != null) {
            return drinksLiveData.getValue();
        }
        return Collections.emptyList();
    }

}
