package com.ikvych.cocktail.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.presentation.adapter.list.CocktailAdapter
import com.ikvych.cocktail.presentation.adapter.list.ItemViewLook
import com.ikvych.cocktail.presentation.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.cocktail.CocktailViewModel
import com.ikvych.cocktail.viewmodel.cocktail.MainFragmentViewModel


abstract class RecyclerViewFragment<ViewModel : CocktailViewModel, DataBinding : ViewDataBinding> :
    BaseFragment<ViewModel, DataBinding>() {
    protected lateinit var cocktailAdapter: CocktailAdapter
    private lateinit var recyclerView: RecyclerView

    abstract val itemViewType: ItemViewLook
    abstract val recyclerViewId: Int

    val parentViewModel: MainFragmentViewModel
        get() {
            return ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
        }

    override fun configureView(view: View, savedInstanceState: Bundle?) {
        super.configureView(view, savedInstanceState)
        recyclerView = requireView().findViewById(recyclerViewId)
        cocktailAdapter = CocktailAdapter(
            viewModel,
            requireContext(),
            itemViewType,
            recyclerView
        )
        recyclerView.adapter = cocktailAdapter
        cocktailAdapter.updateListData(parentViewModel.cocktailsLiveData.value ?: arrayListOf())
        initLiveDataObserver()
    }

    open fun initLiveDataObserver() {
        parentViewModel.filteredAndSortedDrinksLiveData.observe(this, Observer { cocktails ->
            cocktailAdapter.sortType = parentViewModel.sortTypeLiveData.value!!
            cocktailAdapter.updateListData(cocktails)
        })
    }
}

