package com.ikvych.cocktail.ui.fragment

import android.content.res.Configuration
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.adapter.list.DrinkAdapter
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.ui.base.BaseFragment
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

abstract class RecyclerViewFragment<T : BaseViewModel> : BaseFragment() {
    private lateinit var drinkAdapter: DrinkAdapter
    lateinit var viewModel: T

    fun initViewModel(viewModelClass: Class<T>) {
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }

    fun initLiveDataObserver() {
        viewModel.getLiveData().observe(this, Observer { drinks ->
            drinkAdapter.drinkList = drinks
            determineVisibleLayerOnUpdateData(drinks)
        })
    }

    fun initRecyclerView(drinks: List<Drink>, recyclerViewId: Int, modelType: String) {
        val recyclerView: RecyclerView = requireView().findViewById(recyclerViewId)
        drinkAdapter = DrinkAdapter(requireContext(), modelType)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = drinkAdapter

        determineVisibleLayerOnCreate(drinks)

        drinkAdapter.drinkList = drinks
    }

    fun filterData(vararg drinkFilters: DrinkFilter) {
        var drinks: List<Drink> = viewModel.getCurrentData().toMutableList()
        drinkFilters.forEach {
            when (it.type) {
                DrinkFilterType.ALCOHOL -> {
                    drinks = drinks.filter { drink ->
                        drink.getStrAlcoholic() == it.key
                    }
                }
                DrinkFilterType.CATEGORY -> {
                    drinks = drinks.filter { drink ->
                        drink.getStrCategory() == it.key
                    }
                }
                DrinkFilterType.INGREDIENT -> {
                }
                DrinkFilterType.GLASS -> {
                }
            }
        }
        drinkAdapter.drinkList = drinks
    }

    /**
     * If drinks is empty, hide recyclerView and show appropriate textView,
     * else make recyclerView visible.
     *
     * To do this, use methods from ActivityUtil in util package
     *
     * @param drinks data list to check for items
     */
    open fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        //TO DO
    }

    /**
     * If drinks is empty, hide recyclerView and show appropriate textView,
     * else make recyclerView visible
     *
     * To do this, use methods from ActivityUtil in util package
     *
     * @param drinks data list to check for items
     */
    open fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        //TO DO
    }
}