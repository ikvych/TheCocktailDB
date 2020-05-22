package com.ikvych.cocktail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.viewmodel.MainActivityViewModel;
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel;

public class DrinkDetails extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private SearchActivityViewModel searchActivityViewModel;
    private Drink drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("drink")) {

            drink = intent.getParcelableExtra("drink");
            String viewModelType = intent.getStringExtra("viewModelType");

            switch (viewModelType) {
                case "main":
                    break;
                case "search":
                    initSearchViewModel(drink);
                    break;
            }
        }

        ActivityDrinkDetailsBinding activityDrinkDetailsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_drink_details);

        activityDrinkDetailsBinding.setDrink(drink);
    }

    private void initSearchViewModel(Drink drink) {
        mainActivityViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);

        mainActivityViewModel.saveDrink(drink);

    }


}
