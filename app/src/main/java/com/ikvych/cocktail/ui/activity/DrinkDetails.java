package com.ikvych.cocktail.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding;
import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.viewmodel.MainActivityViewModel;

import static com.ikvych.cocktail.ect.adapter.DrinkAdapter.DRINK;
import static com.ikvych.cocktail.ect.adapter.DrinkAdapter.MAIN_MODEL_TYPE;
import static com.ikvych.cocktail.ect.adapter.DrinkAdapter.SEARCH_MODEL_TYPE;
import static com.ikvych.cocktail.ect.adapter.DrinkAdapter.VIEW_MODEL_TYPE;

public class DrinkDetails extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private Drink drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(DRINK)) {

            drink = intent.getParcelableExtra(DRINK);

            if (intent.hasExtra(VIEW_MODEL_TYPE)) {
                String viewModelType = intent.getStringExtra(VIEW_MODEL_TYPE);
                switch (viewModelType) {
                    case MAIN_MODEL_TYPE:
                        break;
                    case SEARCH_MODEL_TYPE:
                        saveDrinkIntoDb(drink);
                        break;
                }
            }
        }

        ActivityDrinkDetailsBinding activityDrinkDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_drink_details);
        activityDrinkDetailsBinding.setDrink(drink);
    }

    private void saveDrinkIntoDb(Drink drink) {
        mainActivityViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);

        mainActivityViewModel.saveDrink(drink);
    }


    public void resumePreviousActivity(View view) {
        finish();
    }
}
