package com.ikvych.cocktail.presentation.fragment

import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.FragmentFavoriteBinding
import com.ikvych.cocktail.viewmodel.DrinkViewModel
import kotlin.reflect.KClass

class FavoriteFragment : RecyclerViewFragment<DrinkViewModel, FragmentFavoriteBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_favorite
    override val viewModelClass: KClass<DrinkViewModel>
    get() = DrinkViewModel::class
    override val recyclerViewId: Int = R.id.rv_search_result

    override fun initLiveDataObserver() {
        parentViewModel.filteredAndSortedFavoriteDrinksLiveData.observe(this, Observer { cocktails ->
            cocktailAdapter.listData = cocktails
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