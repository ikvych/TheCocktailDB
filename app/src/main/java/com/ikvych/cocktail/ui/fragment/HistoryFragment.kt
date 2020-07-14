package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.network.model.Drink
import com.ikvych.cocktail.databinding.FragmentHistoryBinding
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlin.reflect.KClass

class HistoryFragment : RecyclerViewFragment<BaseViewModel, FragmentHistoryBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_history
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class
    private lateinit var fragmentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        fragmentView = view
        initRecyclerView(view, parentViewModel.drinksLiveData.value ?: emptyList(), R.id.rv_search_result)
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