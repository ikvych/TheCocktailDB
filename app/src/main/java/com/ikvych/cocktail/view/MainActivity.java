package com.ikvych.cocktail.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikvych.cocktail.R;
import com.ikvych.cocktail.adapter.DrinkAdapter;
import com.ikvych.cocktail.databinding.ActivityMainBinding;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.util.ActivityUtil;
import com.ikvych.cocktail.viewmodel.MainActivityViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrinkAdapter drinkAdapter;
    private MainActivityViewModel viewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        initRecycleView(viewModel.getCurrentData());
        initLiveDataObserver();
        initFabListener();
    }

    private void initLiveDataObserver() {
        viewModel.getDrinksLiveData().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {
                drinkAdapter.setDrinkList(drinks);
                if (drinks.size() == 0) {
                    ActivityUtil.setDbEmptyHistoryVisible(MainActivity.this);
                } else {
                    ActivityUtil.setDbRecyclerViewVisible(MainActivity.this);
                }
            }
        });
    }

    private void initFabListener() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void initRecycleView(List<Drink> drinks) {
        RecyclerView recyclerView = activityMainBinding.dbRecyclerView;
        drinkAdapter = new DrinkAdapter(this, DrinkAdapter.MAIN_MODEL_TYPE);
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);

        if (drinks.size() == 0) {
            ActivityUtil.setDbEmptyHistoryVisible(MainActivity.this);
        } else {
            ActivityUtil.setDbRecyclerViewVisible(MainActivity.this);
        }

        drinkAdapter.setDrinkList(drinks);
    }
}
