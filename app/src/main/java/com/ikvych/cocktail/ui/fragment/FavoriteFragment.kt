package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.COCKTAIL_ID
import com.ikvych.cocktail.databinding.FragmentFavoriteBinding
import com.ikvych.cocktail.ui.activity.DrinkDetailActivity
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlin.reflect.KClass

class FavoriteFragment() : RecyclerViewFragment<BaseViewModel, FragmentFavoriteBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_favorite
    override val viewModelClass: KClass<BaseViewModel>
    get() = BaseViewModel::class
    private lateinit var fragmentView: View

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        fragmentView = view
        initRecyclerView(
            view,
            parentViewModel.filteredFavoriteCocktailsLiveData.value ?: emptyList(),
            R.id.rv_search_result
        )
        initLiveDataObserver()
    }

    override fun initLiveDataObserver() {
        parentViewModel.filteredFavoriteCocktailsLiveData.observe(this, Observer { cocktails ->
            cocktailAdapter.listData = cocktails
            determineVisibleLayerOnUpdateData(cocktails)
        })
        viewModel.startCocktailDetailsLiveData.observe(this, Observer {
            if (it != null) {
                val intent = Intent(requireActivity(), DrinkDetailActivity::class.java)
                intent.putExtra(COCKTAIL_ID, it.id)
                startActivity(intent)
            }
        })
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

}