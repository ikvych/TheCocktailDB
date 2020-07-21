package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class HistoryFragment : RecyclerViewFragment<BaseViewModel>() {

    override var contentLayoutResId: Int = R.layout.fragment_history
    override val viewModel: BaseViewModel by viewModels()

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        initRecyclerView(
            view,
            parentViewModel.drinksLiveData.value ?: emptyList(),
            R.id.rv_search_result
        )
    }

    override fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(requireView())
        } else {
            setDbRecyclerViewVisible(requireView())
        }
    }

    override fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(requireView())
        } else {
            setDbRecyclerViewVisible(requireView())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }

}