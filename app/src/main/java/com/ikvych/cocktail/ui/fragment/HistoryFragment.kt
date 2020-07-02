package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.FragmentHistoryBinding
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class HistoryFragment : RecyclerViewFragment<BaseViewModel, FragmentHistoryBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_history
    override val viewModel: BaseViewModel by viewModels()

    private lateinit var fragmentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        fragmentView = view
        initRecyclerView(view, parentViewModel.getAllDrinksFromDb(), R.id.rv_search_result)
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

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }

}