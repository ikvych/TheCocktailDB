package com.ikvych.cocktail.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.presentation.adapter.list.CocktailAdapterTest
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel
import com.ikvych.cocktail.viewmodel.cocktail.MainFragmentViewModel

abstract class RecyclerViewFragment<ViewModel : CocktailViewModel, DataBinding : ViewDataBinding> :
    BaseFragment<ViewModel, DataBinding>() {
    protected lateinit var cocktailAdapter: CocktailAdapterTest
    private var layoutManager: GridLayoutManager? = null
    abstract val isFavorite: Boolean

    val parentViewModel: MainFragmentViewModel
        get() {
            return ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
        }
    abstract val recyclerViewId: Int

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        if (!isFavorite) {
            layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    GridLayoutManager(requireContext(), 2)
                } else {
                    GridLayoutManager(requireContext(), 4)
                }
            cocktailAdapter = CocktailAdapterTest(
                viewModel,
                requireContext(),
                layoutManager = layoutManager!!,
                isFavorite = isFavorite
            )
        } else {
            layoutManager = GridLayoutManager(requireContext(), 1)
            cocktailAdapter = CocktailAdapterTest(
                viewModel,
                requireContext(),
                layoutManager = layoutManager!!,
                isFavorite = isFavorite
            )
        }

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
        recyclerView.layoutManager = layoutManager ?: LinearLayoutManager(requireContext())
        recyclerView.adapter = cocktailAdapter
        cocktailAdapter.listData = parentViewModel.cocktailsLiveData.value ?: arrayListOf()
    }
}

