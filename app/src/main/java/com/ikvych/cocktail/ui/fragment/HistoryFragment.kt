package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelLazy
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentHistoryBinding
import com.ikvych.cocktail.di.Injector
import com.ikvych.cocktail.ui.extension.baseViewModels
import com.ikvych.cocktail.ui.extension.viewModels
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlin.reflect.KClass

class HistoryFragment() : RecyclerViewFragment<BaseViewModel, FragmentHistoryBinding>() {

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
        initRecyclerView(
            view,
            parentViewModel.cocktailLiveData.value ?: emptyList(),
            R.id.rv_search_result
        )
        initLiveDataObserver()
    }

    override fun determineVisibleLayerOnCreate(cocktails: List<CocktailModel?>?) {
        if (cocktails!!.isEmpty()) {
            setDbEmptyHistoryVisible(fragmentView)
        } else {
            setDbRecyclerViewVisible(fragmentView)
        }
    }

    override fun determineVisibleLayerOnUpdateData(cocktails: List<CocktailModel?>?) {
        if (cocktails!!.isEmpty()) {
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