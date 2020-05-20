package com.ikvych.cocktail.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.repository.DrinkApiRepository;

public class MainActivity extends AppCompatActivity {

    private DrinkApiRepository drinkRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drinkRepository = new DrinkApiRepository();
        drinkRepository.getDrinkListByName("Margarita");
    }
}
