package com.ikvych.cocktail.ui.base;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikvych.cocktail.ect.adapter.DrinkAdapter;
import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.viewmodel.base.BaseViewModel;

import java.util.List;

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {

    protected DrinkAdapter drinkAdapter;
    protected T viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initViewModel(Class<T> viewModelClass) {
        viewModel = new ViewModelProvider(this).get(viewModelClass);
    }

    protected void initLiveDataObserver() {
        viewModel.getDrinksLiveData().observe(this, drinks -> {
            drinkAdapter.setDrinkList(drinks);
            setLayerVisibilityOnUpdateData(drinks);
        });
    }

    public void initRecycleView(List<Drink> drinks, Integer recyclerViewId, String ModelType) {
        RecyclerView recyclerView = findViewById(recyclerViewId);
        drinkAdapter = new DrinkAdapter(this, ModelType);
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);

        setLayerVisibilityOnCreateData(drinks);

        drinkAdapter.setDrinkList(drinks);
    }

    protected void setLayerVisibilityOnCreateData(List<Drink> drinks) {
        //TO DO
    }

    protected void setLayerVisibilityOnUpdateData(List<Drink> drinks) {
        //TO DO
    }

}
