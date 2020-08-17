package com.ikvych.cocktail.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.presentation.adapter.list.CocktailAdapterTest
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel
import com.ikvych.cocktail.viewmodel.cocktail.MainFragmentViewModel

abstract class RecyclerViewFragment<ViewModel : CocktailViewModel, DataBinding : ViewDataBinding> :
    BaseFragment<ViewModel, DataBinding>() {
    protected lateinit var cocktailAdapter: CocktailAdapterTest
    private lateinit var layoutManager: GridLayoutManager

    val parentViewModel: MainFragmentViewModel
        get() {
            return ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
        }
    abstract val recyclerViewId: Int

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            GridLayoutManager(requireContext(), 2)
        } else {
            GridLayoutManager(requireContext(), 4)
        }
        cocktailAdapter = CocktailAdapterTest(viewModel, requireContext(), layoutManager = layoutManager)
        initLiveDataObserver()
        initRecyclerView()
    }

    open fun initLiveDataObserver() {
        parentViewModel.filteredAndSortedDrinksLiveData.observe(this, Observer { cocktails ->
            cocktailAdapter.sortType = parentViewModel.sortTypeLiveData.value!!
            cocktailAdapter.listData = cocktails
        })
    }

    fun initRecyclerView() {
        val recyclerView: RecyclerView = requireView().findViewById(recyclerViewId)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = cocktailAdapter
        cocktailAdapter.listData = parentViewModel.cocktailsLiveData.value ?: arrayListOf()
    }
}

