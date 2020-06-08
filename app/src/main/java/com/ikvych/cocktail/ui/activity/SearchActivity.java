package com.ikvych.cocktail.ui.activity;

import android.os.Bundle;
import android.widget.SearchView;

import com.ikvych.cocktail.R;
import com.ikvych.cocktail.data.entity.Drink;
import com.ikvych.cocktail.ect.adapter.DrinkAdapter;
import com.ikvych.cocktail.ect.util.ActivityUtil;
import com.ikvych.cocktail.ui.base.BaseActivity;
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchActivityViewModel> {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViewModel(SearchActivityViewModel.class);
        initSearchView();
        initRecycleView(viewModel.getCurrentData(), R.id.search_recycler_view, DrinkAdapter.SEARCH_MODEL_TYPE);
        initLiveDataObserver();
    }

    @Override
    protected void determineVisibleLayerOnCreate(List<Drink> drinks) {
        if (drinks.size() == 0) {
            ActivityUtil.setSearchEmptyListVisible(this);
        } else {
            ActivityUtil.setSearchRecyclerViewVisible(this);
        }
    }

    @Override
    protected void determineVisibleLayerOnUpdateData(List<Drink> drinks) {
        if (drinks.size() == 0) {
            ActivityUtil.setEmptySearchVisible(SearchActivity.this);
        } else {
            ActivityUtil.setSearchRecyclerViewVisible(SearchActivity.this);
        }
    }


    private void initSearchView() {
        searchView = findViewById(R.id.search_query);
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

}
