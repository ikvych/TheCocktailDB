package com.ikvych.cocktail.presentation.fragment

import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentHistoryBinding
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel
import kotlin.reflect.KClass

class HistoryFragment : RecyclerViewFragment<CocktailViewModel, FragmentHistoryBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_history
    override val viewModelClass: KClass<CocktailViewModel>
        get() = CocktailViewModel::class
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