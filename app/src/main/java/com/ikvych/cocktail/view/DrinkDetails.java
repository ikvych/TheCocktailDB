package com.ikvych.cocktail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ikvych.cocktail.R;
import com.ikvych.cocktail.databinding.ActivityDrinkDetailsBinding;
import com.ikvych.cocktail.model.Drink;

public class DrinkDetails extends AppCompatActivity {

    private Drink drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_details);

        ActivityDrinkDetailsBinding activityDrinkDetailsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_drink_details);

        Intent intent = getIntent();


        if (intent != null && intent.hasExtra("drink")) {
            drink = intent.getParcelableExtra("drink");
            activityDrinkDetailsBinding.setDrink(drink);
        }
    }
}
