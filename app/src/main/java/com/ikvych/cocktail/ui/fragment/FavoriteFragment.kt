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
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class FavoriteFragment : RecyclerViewFragment<BaseViewModel, FragmentFavoriteBinding>() {

    override var contentLayoutResId: Int = R.layout.fragment_favorite
    override val viewModel: BaseViewModel by viewModels()

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

        initRecyclerView(
            view,
            parentViewModel.filteredFavoriteDrinksLiveData.value ?: emptyList(),
            R.id.rv_search_result
        )
        initLiveDataObserver()
    }

    override fun initLiveDataObserver() {
        parentViewModel.filteredFavoriteDrinksLiveData.observe(this, Observer { drinks ->
            drinkAdapter.listData = drinks
            determineVisibleLayerOnUpdateData(drinks)
        })
        viewModel.startDrinkDetailsLiveData.observe(this, Observer {
            if (it != null) {
                val intent = Intent(requireActivity(), DrinkDetailActivity::class.java)
                intent.putExtra(DRINK, it)
                startActivity(intent)
            }
        })
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

}