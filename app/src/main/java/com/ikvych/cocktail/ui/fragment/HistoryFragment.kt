package com.ikvych.cocktail.ui.fragment

import androidx.fragment.app.viewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentHistoryBinding
import com.ikvych.cocktail.viewmodel.DrinkViewModel

class HistoryFragment : RecyclerViewFragment<DrinkViewModel, FragmentHistoryBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_history
    override val viewModel: DrinkViewModel by viewModels()
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