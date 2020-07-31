package com.ikvych.cocktail.ui.fragment

import android.os.Bundle
import android.view.View
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentHistoryBinding
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.DrinkViewModel
import kotlin.reflect.KClass

class HistoryFragment : RecyclerViewFragment<DrinkViewModel, FragmentHistoryBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_history
    override val viewModelClass: KClass<DrinkViewModel>
        get() = DrinkViewModel::class
    override val recyclerViewId: Int = R.id.rv_search_result

    override fun configureDataBinding(binding: FragmentHistoryBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = parentViewModel
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}