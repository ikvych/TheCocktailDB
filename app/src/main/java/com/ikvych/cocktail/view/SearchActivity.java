package com.ikvych.cocktail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

        EditText searchName = findViewById(R.id.search_name);
        searchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String searchQuery = v.getText().toString();
                    findDrinkByName(searchQuery);
                    View view = SearchActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    handled = true;
                }
                return handled;
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
        drinkAdapter = new DrinkAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(drinkAdapter);
        drinkAdapter.notifyDataSetChanged();
    }
}
