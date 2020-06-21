package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.ui.base.ALCOHOL_FILTER_BUNDLE_KEY
import com.ikvych.cocktail.ui.base.ALCOHOL_FILTER_KEY
import com.ikvych.cocktail.ui.base.FRAGMENT_ID
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.MainViewModel

class HistoryFragment : RecyclerViewFragment<MainViewModel>() {

    companion object {

        @JvmStatic
        fun newInstance(fragmentId: Int) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_ID, fragmentId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(ALCOHOL_FILTER_KEY) { _, bundle ->
            val result = bundle.getString(ALCOHOL_FILTER_BUNDLE_KEY)
            filterData(AlcoholDrinkFilter.valueOf(result!!))
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun configureView(savedInstanceState: Bundle?) {
        super.configureView(savedInstanceState)

        initViewModel(MainViewModel::class.java)
        initRecyclerView(viewModel.getCurrentData(), R.id.db_recycler_view, MAIN_MODEL_TYPE)
        initLiveDataObserver()
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
}