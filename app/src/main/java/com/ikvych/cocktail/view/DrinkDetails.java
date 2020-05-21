package com.ikvych.cocktail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ikvych.cocktail.R;
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.viewmodel.MainActivityViewModel;

public class DrinkDetails extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private Drink drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(MainActivityViewModel.class);

        ActivityDrinkDetailsBinding activityDrinkDetailsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_drink_details);

        Intent intent = getIntent();


        if (intent != null && intent.hasExtra("drink")) {
            drink = intent.getParcelableExtra("drink");
            viewModel.insertDrinkIntoDB(drink);
            activityDrinkDetailsBinding.setDrink(drink);
        }
    }
}
