package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.DRINK
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.constant.VIEW_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.FragmentFavoriteBinding
import com.ikvych.cocktail.ui.activity.DrinkDetailActivity
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.DrinkViewModel
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

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