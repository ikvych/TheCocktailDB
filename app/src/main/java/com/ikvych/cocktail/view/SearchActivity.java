package com.ikvych.cocktail.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.adapter.DrinkAdapter;
import com.ikvych.cocktail.databinding.ActivitySearchBinding;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private DrinkAdapter drinkAdapter;
    private SearchActivityViewModel viewModel;
    private ActivitySearchBinding activitySearchBinding;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        viewModel = new ViewModelProvider(this).get(SearchActivityViewModel.class);

        initRecycleView(viewModel.getDataOnRotation());
        initSearchView();
    }

    public void initSearchView() {
        searchView = findViewById(R.id.search_name);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchQuery = query.trim();
                if (searchQuery.equals("")) {
                    return false;
                } else {
                    findDrinkByName(searchQuery);
                    searchView.clearFocus();
                    return true;
                }
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

    public void initRecycleView(List<Drink> drinks) {
        RecyclerView recyclerView = activitySearchBinding.recyclerView;
        drinkAdapter = new DrinkAdapter(this, "search");
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);
        drinkAdapter.setDrinkList(drinks);
    }
}
