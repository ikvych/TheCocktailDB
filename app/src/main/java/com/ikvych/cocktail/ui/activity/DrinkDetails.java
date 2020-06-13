package com.ikvych.cocktail.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding;
import com.ikvych.cocktail.viewmodel.MainActivityViewModel;

import static com.ikvych.cocktail.adapter.list.DrinkAdapter.DRINK;
import static com.ikvych.cocktail.adapter.list.DrinkAdapter.MAIN_MODEL_TYPE;
import static com.ikvych.cocktail.adapter.list.DrinkAdapter.SEARCH_MODEL_TYPE;
import static com.ikvych.cocktail.adapter.list.DrinkAdapter.VIEW_MODEL_TYPE;

public class DrinkDetails extends AppCompatActivity {

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
                if (viewModelType != null) {
                    switch (viewModelType) {
                        case MAIN_MODEL_TYPE:
                            break;
                        case SEARCH_MODEL_TYPE:
                            saveDrinkIntoDb(drink);
                            break;
                    }
                }
            }
        }

        ActivityDrinkDetailsBinding activityDrinkDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_drink_details);
        activityDrinkDetailsBinding.setDrink(drink);
    }

    private void saveDrinkIntoDb(Drink drink) {
        MainActivityViewModel mainActivityViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);

        mainActivityViewModel.saveDrink(drink);
    }


    @SuppressWarnings("unused")
    public void resumePreviousActivity(View view) {
        finish();
    }
}
