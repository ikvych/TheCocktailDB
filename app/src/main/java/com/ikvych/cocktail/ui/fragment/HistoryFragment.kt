package com.ikvych.cocktail.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.ikvych.cocktail.R
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.listener.FilterResultCallBack
import com.ikvych.cocktail.listener.SortResultCallBack
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.MainViewModel

class HistoryFragment : RecyclerViewFragment<MainViewModel>(),
    FilterFragment.OnFilterResultListener, MainFragment.OnSortResultListener {

    private lateinit var fragmentView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            (context as FilterResultCallBack).addCallBack(this)
            (parentFragment as SortResultCallBack).addCallBack(this)
        } catch (exception: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement FilterResultCallBack")
        }
    }

    override fun onDetach() {
        super.onDetach()
        try {
            (requireActivity() as FilterResultCallBack).removeCallBack(this)
            (parentFragment as SortResultCallBack).removeCallBack(this)
        } catch (exception: ClassCastException) {
            throw ClassCastException("${activity.toString()} must implement FilterResultCallBack")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(MainViewModel::class.java)
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        fragmentView = view
        initRecyclerView(view, viewModel.getCurrentData(), R.id.db_recycler_view)
        initLiveDataObserver()
    }


    override fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(fragmentView)
        } else {
            setDbRecyclerViewVisible(fragmentView)
        }
    }

    override fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(fragmentView)
        } else {
            setDbRecyclerViewVisible(fragmentView)
        }
    }

    override fun onFilterApply(drinkFilters: ArrayList<DrinkFilter>) {
        filters = drinkFilters
        filterData(viewModel.getCurrentData(), filters)
        sortData(sortDrinkType)
    }

    override fun onFilterReset() {
        filters = arrayListOf()
        filterData(viewModel.getCurrentData(), filters)
        sortData(sortDrinkType)
    }

    override fun onResult(sortDrinkType: SortDrinkType) {
        super.sortDrinkType = sortDrinkType
        sortData(sortDrinkType)
    }

    companion object {
        @JvmStatic
        fun newInstance(fragmentId: Int) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }
}