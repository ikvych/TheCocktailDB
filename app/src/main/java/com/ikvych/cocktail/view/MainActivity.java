package com.ikvych.cocktail.view;

import android.content.Intent;
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
import com.ikvych.cocktail.viewmodel.MainActivityViewModel;
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel;

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

        fillRecycleView(viewModel.getCurrentData());

        viewModel.getDrinksLiveData().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {
                drinkAdapter.setDrinkList(drinks);
                if (drinks.size() == 0) {
                    findViewById(R.id.empty_history).setVisibility(View.VISIBLE);
                    findViewById(R.id.recyclerView).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.empty_history).setVisibility(View.GONE);
                    findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    public void fillRecycleView(List<Drink> drinks) {
        RecyclerView recyclerView = activityMainBinding.recyclerView;
        drinkAdapter = new DrinkAdapter(this, "main");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);
        if (drinks.size() == 0) {
            findViewById(R.id.empty_history).setVisibility(View.VISIBLE);
            findViewById(R.id.recyclerView).setVisibility(View.GONE);
        } else {
            findViewById(R.id.empty_history).setVisibility(View.GONE);
            findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
        }
        drinkAdapter.setDrinkList(drinks);
    }
}
