package com.ikvych.cocktail.ui.base;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.adapter.list.DrinkAdapter;
import com.ikvych.cocktail.util.ActivityUtil;
import com.ikvych.cocktail.viewmodel.base.BaseViewModel;

import java.util.List;

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {

    private DrinkAdapter drinkAdapter;
    protected T viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initViewModel(Class<T> viewModelClass) {
        viewModel = new ViewModelProvider(this).get(viewModelClass) ;
    }

    protected void initLiveDataObserver() {
        viewModel.getDrinksLiveData().observe(this, drinks -> {
            drinkAdapter.setDrinkList(drinks );
            determineVisibleLayerOnUpdateData(drinks);
        });
    }

    protected void initRecycleView(List<Drink> drinks, Integer recyclerViewId, String ModelType) {
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

        determineVisibleLayerOnCreate(drinks);

        drinkAdapter.setDrinkList(drinks);
    }

    /**
     * If drinks is empty, hide recyclerView and show appropriate textView,
     * else make recyclerView visible.
     *
     * To do this, use {@link ActivityUtil ActivityUtil} methods
     *
     * @param drinks data list to check for items
     */
    protected void determineVisibleLayerOnCreate(List<Drink> drinks) {
        //TO DO
    }

    /**
     * If drinks is empty, hide recyclerView and show appropriate textView,
     * else make recyclerView visible
     *
     * To do this, use {@link ActivityUtil ActivityUtil} methods
     *
     * @param drinks data list to check for items
     */
    protected void determineVisibleLayerOnUpdateData(List<Drink> drinks) {
        //TO DO
    }

}
