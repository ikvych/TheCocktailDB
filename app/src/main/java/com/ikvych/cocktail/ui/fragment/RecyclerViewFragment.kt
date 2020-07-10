package com.ikvych.cocktail.ui.fragment

import android.content.res.Configuration
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.adapter.list.DrinkAdapter
import com.ikvych.cocktail.comparator.AlcoholDrinkComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.filter.type.DrinkFilterType
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

abstract class RecyclerViewFragment<T : BaseViewModel> : BaseFragment() {
    protected lateinit var drinkAdapter: DrinkAdapter
    lateinit var viewModel: T
    var filters: ArrayList<DrinkFilter> = arrayListOf()
    protected var sortDrinkType: SortDrinkType = SortDrinkType.RECENT

    private val alcoholComparator: AlcoholDrinkComparator = AlcoholDrinkComparator()

    fun initViewModel(viewModelClass: Class<T>) {
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }

    open fun initLiveDataObserver() {
        viewModel.getLiveData().observe(this, Observer { drinks ->
            filterData(drinks, filters)
            sortData(sortDrinkType)
            determineVisibleLayerOnUpdateData(drinks)
        })
    }

    fun initRecyclerView(view: View, drinks: List<Drink>, recyclerViewId: Int) {
        val recyclerView: RecyclerView = view.findViewById(recyclerViewId)
        drinkAdapter = DrinkAdapter(requireContext())

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = drinkAdapter

        determineVisibleLayerOnCreate(drinks)

        drinkAdapter.drinkList = drinks
    }

    fun filterData(drinks: List<Drink>, drinkFilters: ArrayList<DrinkFilter>) {
        var drinksCopy = drinks
        drinkFilters.forEach {
            when (it.type) {
                DrinkFilterType.ALCOHOL -> {
                    drinksCopy = drinksCopy.filter { drink ->
                        drink.getStrAlcoholic() == it.key
                    }
                }
                DrinkFilterType.CATEGORY -> {
                    drinksCopy = drinksCopy.filter { drink ->
                        drink.getStrCategory() == it.key
                    }
                }
                DrinkFilterType.INGREDIENT -> {
                    drinksCopy = drinksCopy.filter { drink ->
                        var isValid = false
                        for ((key, _) in drink.getIngredients()) {
                            if (key.equals(it.key)) {
                                isValid = true
                                break
                            }
                        }
                        isValid
                    }
                }
                DrinkFilterType.GLASS -> {
                }
            }
        }
        drinkAdapter.drinkList = drinksCopy
        determineVisibleLayerOnUpdateData(drinksCopy)
    }

    fun sortData(sortDrinkType: SortDrinkType) {
        var drinksCopy = drinkAdapter.drinkList
        drinksCopy = when (sortDrinkType) {
            SortDrinkType.RECENT -> drinksCopy.sortedByDescending { drink -> drink.getCreated() }
            SortDrinkType.NAME_ASC -> drinksCopy.sortedBy { drink -> drink.getStrDrink() }
            SortDrinkType.NAME_DESC -> drinksCopy.sortedByDescending { drink -> drink.getStrDrink() }
            SortDrinkType.ALCOHOL_ASC -> drinksCopy.sortedWith(alcoholComparator)
            SortDrinkType.ALCOHOL_DESC -> drinksCopy.sortedWith(alcoholComparator).asReversed()
            SortDrinkType.INGREDIENT_COUNT_ASC -> drinksCopy.sortedBy { drink -> drink.getIngredients().size }
            SortDrinkType.INGREDIENT_COUNT_DESC -> drinksCopy.sortedByDescending { drink -> drink.getIngredients().size }
        }
        drinkAdapter.drinkList = drinksCopy
        determineVisibleLayerOnUpdateData(drinksCopy)
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