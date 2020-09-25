package com.ikvych.cocktail.cocktail

import androidx.lifecycle.Observer
import com.ikvych.cocktail.cocktail.databinding.FragmentFavoriteBinding
import com.ikvych.cocktail.prresentation.adapter.list.ItemViewLook
import com.ikvych.cocktail.prresentation.viewmodel.cocktail.CocktailViewModel
import kotlin.reflect.KClass

class FavoriteFragment : RecyclerViewFragment<CocktailViewModel, FragmentFavoriteBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_favorite
    override val viewModelClass: KClass<CocktailViewModel>
    get() = CocktailViewModel::class
    override val recyclerViewId: Int = R.id.rv_search_result
    override val itemViewType: ItemViewLook
        get() = ItemViewLook.LIST_ITEM

    override fun initLiveDataObserver() {
        parentViewModel.filteredAndSortedFavoriteDrinksLiveData.observe(this, Observer { cocktails ->
            cocktailAdapter.sortType = parentViewModel.sortTypeLiveData.value!!
            cocktailAdapter.updateListData(cocktails)
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