package com.ikvych.cocktail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.adapter.DrinkAdapter;
import com.ikvych.cocktail.databinding.ActivitySearchBinding;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.repository.DrinkApiRepository;
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DrinkAdapter drinkAdapter;
    private SearchActivityViewModel viewModel;
    private ActivitySearchBinding activitySearchBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        viewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(SearchActivityViewModel.class);

        fillRecycleView();


        getApplicationContext();

        SearchView searchView = findViewById(R.id.search_name);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean handled = false;

                String searchQuery = query.trim();
                findDrinkByName(searchQuery);
                searchView.clearFocus();
                handled = true;

                return handled;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    public void findDrinkByName(String name) {
        viewModel.getDrinksByNameData(name).observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {
                drinkAdapter.setDrinkList(drinks);
            }
        });
    }

    public void fillRecycleView() {
        RecyclerView recyclerView = activitySearchBinding.recyclerView;
        drinkAdapter = new DrinkAdapter(this, "search");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);
        drinkAdapter.notifyDataSetChanged();
    }
}
