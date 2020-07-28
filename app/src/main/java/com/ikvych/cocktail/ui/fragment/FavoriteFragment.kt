package com.ikvych.cocktail.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentFavoriteBinding
import com.ikvych.cocktail.viewmodel.DrinkViewModel

class FavoriteFragment : RecyclerViewFragment<DrinkViewModel, FragmentFavoriteBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_favorite
    override val viewModel: DrinkViewModel by viewModels()
    override val recyclerViewId: Int = R.id.rv_search_result

    override fun initLiveDataObserver() {
        parentViewModel.filteredAndSortedFavoriteDrinksLiveData.observe(this, Observer { drinks ->
            drinkAdapter.listData = drinks
        })
    }

    override fun configureDataBinding(binding: FragmentFavoriteBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = parentViewModel
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }
}