package com.ikvych.cocktail.view;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ikvych.cocktail.R;
import com.ikvych.cocktail.adapter.DrinkAdapter;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.util.ActivityUtil;
import com.ikvych.cocktail.viewmodel.MainActivityViewModel;

import java.util.List;


public class MainActivity extends BaseActivity<MainActivityViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel(MainActivityViewModel.class);
        initRecycleView(viewModel.getCurrentData(), R.id.db_recycler_view, DrinkAdapter.MAIN_MODEL_TYPE);
        initLiveDataObserver();
        initFabListener();
    }

    @Override
    protected void setLayerVisibilityOnCreateData(List<Drink> drinks) {
        if (drinks.size() == 0) {
            ActivityUtil.setDbEmptyHistoryVisible(this);
        } else {
            ActivityUtil.setDbRecyclerViewVisible(this);
        }
    }

    @Override
    protected void setLayerVisibilityOnUpdateData(List<Drink> drinks) {
        if (drinks.size() == 0) {
            ActivityUtil.setDbEmptyHistoryVisible(MainActivity.this);
        } else {
            ActivityUtil.setDbRecyclerViewVisible(MainActivity.this);
        }
    }

    private void initFabListener() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            MainActivity.this.startActivity(intent);
        });
    }

}
