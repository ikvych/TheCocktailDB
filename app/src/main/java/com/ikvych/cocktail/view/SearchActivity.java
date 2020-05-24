package com.ikvych.cocktail.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.adapter.DrinkAdapter;
import com.ikvych.cocktail.model.Drink;
import com.ikvych.cocktail.util.ActivityUtil;
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel;

import java.util.List;

public class SearchActivity extends FragmentActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;

    private DrinkAdapter drinkAdapter;
    private SearchActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        viewModel = new ViewModelProvider(this).get(SearchActivityViewModel.class);

        initSearchView();
        initRecycleView(viewModel.getCurrentData());
        findDrinkByName();
    }


    public void initSearchView() {
        searchView = findViewById(R.id.search_name);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchQuery = query.trim();
                viewModel.updateDrinksLiveData(searchQuery);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
    }

    public void findDrinkByName() {
        viewModel.getDrinksLiveData().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(List<Drink> drinks) {
                drinkAdapter.setDrinkList(drinks);
                if (drinks.size() == 0) {
                    ActivityUtil.setEmptySearchVisible(SearchActivity.this);
                } else {
                    ActivityUtil.setSearchRecyclerViewVisible(SearchActivity.this);
                }
            }
        });
    }


    public void initRecycleView(List<Drink> drinks) {
        recyclerView = findViewById(R.id.search_recycler_view);
        drinkAdapter = new DrinkAdapter(this, "search");
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);
        if (drinks.size() == 0) {
            ActivityUtil.setSearchEmptyListVisible(this);
        } else {
            ActivityUtil.setSearchRecyclerViewVisible(this);
        }
        drinkAdapter.setDrinkList(drinks);
    }
}
