package com.ikvych.cocktail.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.presentation.adapter.list.CocktailAdapter
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.DrinkViewModel
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel

abstract class RecyclerViewFragment<ViewModel : DrinkViewModel, DataBinding : ViewDataBinding> :
    BaseFragment<ViewModel, DataBinding>() {
    protected lateinit var cocktailAdapter: CocktailAdapter

    val parentViewModel: MainFragmentViewModel
        get() {
            return ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
        }
    abstract val recyclerViewId: Int

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        cocktailAdapter = CocktailAdapter(viewModel, requireContext())
        initLiveDataObserver()
        initRecyclerView()
    }

    open fun initLiveDataObserver() {
        parentViewModel.filteredAndSortedDrinksLiveData.observe(this, Observer { cocktails ->
            cocktailAdapter.listData = cocktails
        })
    }

    fun initRecyclerView() {
        val recyclerView: RecyclerView = requireView().findViewById(recyclerViewId)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        }
        recyclerView.adapter = cocktailAdapter
        cocktailAdapter.listData = parentViewModel.cocktailsLiveData.value ?: arrayListOf()
    }

    override fun onDestroy() {
        super.onDestroy()
        cocktailAdapter.setLifecycleDestroyed()
    }
}

