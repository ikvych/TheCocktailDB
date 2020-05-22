package com.ikvych.cocktail.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);

        fillRecycleView();

        viewModel.getAllDrinksFromDb().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {
                drinkAdapter.setDrinkList(drinks);
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

    public void fillRecycleView() {
        RecyclerView recyclerView = activityMainBinding.recyclerView;
        drinkAdapter = new DrinkAdapter(this, "main");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);
        drinkAdapter.notifyDataSetChanged();
    }
}
