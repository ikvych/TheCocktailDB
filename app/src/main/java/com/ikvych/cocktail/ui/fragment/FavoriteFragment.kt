package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.listener.FilterResultCallBack
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.MainViewModel

class FavoriteFragment : RecyclerViewFragment<MainViewModel>(), FilterFragment.OnFilterResultListener {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            (requireActivity() as FilterResultCallBack).addCallBack(this)
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement FilterResultCallBack")
        }
    }

    override fun onDetach() {
        super.onDetach()
        try {
            (requireActivity() as FilterResultCallBack).removeCallBack(this)
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement FilterResultCallBack")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentId: Int) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }

    override fun configureView(savedInstanceState: Bundle?) {
        super.configureView(savedInstanceState)

        initViewModel(MainViewModel::class.java)
        initRecyclerView(viewModel.getFavoriteCurrentData(), R.id.db_recycler_view, MAIN_MODEL_TYPE)
        initLiveDataObserver()
    }

    override fun initLiveDataObserver() {
        viewModel.getFavoriteLiveData().observe(this, Observer { drinks ->
            drinkAdapter.drinkList = drinks
            determineVisibleLayerOnUpdateData(drinks)
        })
    }

    override fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(requireActivity())
        } else {
            setDbRecyclerViewVisible(requireActivity())
        }
    }

    override fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(requireActivity())
        } else {
            setDbRecyclerViewVisible(requireActivity())
        }
    }

    override fun onFilterApply(vararg drinkFilters: DrinkFilter) {
        filterData(*drinkFilters)
    }

    override fun onFilterRest() {

    }

    override fun filterData(vararg drinkFilters: DrinkFilter) {
        var drinks: List<Drink> = viewModel.getFavoriteCurrentData().toMutableList()
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
}