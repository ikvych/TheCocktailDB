package com.ikvych.cocktail.cocktail

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.prresentation.adapter.list.CocktailAdapter
import com.ikvych.cocktail.prresentation.adapter.list.ItemViewLook
import com.ikvych.cocktail.prresentation.ui.base.fragment.BaseFragment
import com.ikvych.cocktail.prresentation.viewmodel.cocktail.CocktailViewModel

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

