package com.ikvych.cocktail.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.adapter.list.DrinkAdapter
import com.ikvych.cocktail.comparator.AlcoholDrinkComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.constant.DRINK
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.constant.SEARCH_MODEL_TYPE
import com.ikvych.cocktail.constant.VIEW_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.ui.activity.DrinkDetailActivity
import com.ikvych.cocktail.ui.base.BaseFragment
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

abstract class RecyclerViewFragment<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> : BaseFragment<ViewModel, DataBinding>() {
    protected lateinit var drinkAdapter: DrinkAdapter

    lateinit var parentViewModel: MainFragmentViewModel
    var filters: ArrayList<DrinkFilter> = arrayListOf()
    protected var sortDrinkType: SortDrinkType = SortDrinkType.RECENT

    private val alcoholComparator: AlcoholDrinkComparator = AlcoholDrinkComparator()

    fun initViewModel() {
        parentViewModel = ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
    }

    open fun initLiveDataObserver() {
        parentViewModel.filteredDrinksLiveData.observe(this, Observer { drinks ->
            drinkAdapter.listData = drinks
            determineVisibleLayerOnUpdateData(drinks)
        })
        viewModel.startDrinkDetailsLiveData.observe(this, Observer {
            if (it != null) {
                val intent = Intent(requireActivity(), DrinkDetailActivity::class.java)
                intent.putExtra(VIEW_MODEL_TYPE, MAIN_MODEL_TYPE)
                intent.putExtra(DRINK, it)
                startActivity(intent)
            }
        })
    }

    fun initRecyclerView(view: View, drinks: List<Drink>, recyclerViewId: Int) {
        val recyclerView: RecyclerView = view.findViewById(recyclerViewId)
        drinkAdapter = DrinkAdapter(viewModel, requireContext())

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = drinkAdapter

        determineVisibleLayerOnCreate(drinks)

        drinkAdapter.listData = drinks
    }

    override fun onDestroy() {
        super.onDestroy()
        drinkAdapter.setLifecycleDestroyed()
    }
    /**
     * If drinks is empty, hide recyclerView and show appropriate textView,
     * else make recyclerView visible.
     *
     * To do this, use methods from ActivityUtil in util package
     *
     * @param drinks data list to check for items
     */
    open fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        //TO DO
    }

    /**
     * If drinks is empty, hide recyclerView and show appropriate textView,
     * else make recyclerView visible
     *
     * To do this, use methods from ActivityUtil in util package
     *
     * @param drinks data list to check for items
     */
    open fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        //TO DO
    }

}