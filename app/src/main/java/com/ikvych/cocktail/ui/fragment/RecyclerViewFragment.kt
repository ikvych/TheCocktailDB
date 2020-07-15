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
import com.ikvych.cocktail.comparator.AlcoholCocktailComparator
import com.ikvych.cocktail.comparator.type.SortDrinkType
import com.ikvych.cocktail.constant.COCKTAIL_ID
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.ui.activity.DrinkDetailActivity
import com.ikvych.cocktail.ui.fragment.base.BaseFragment
import com.ikvych.cocktail.ui.model.cocktail.CocktailModel
import com.ikvych.cocktail.viewmodel.MainFragmentViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

abstract class RecyclerViewFragment<ViewModel : BaseViewModel, DataBinding : ViewDataBinding> :
    BaseFragment<ViewModel, DataBinding>() {
    protected lateinit var cocktailAdapter: DrinkAdapter

    lateinit var parentViewModel: MainFragmentViewModel /*by ViewModelLazy(
        MainFragmentViewModel::class,
        { viewModelStore },
        { Injector.ViewModelFactory(requireActivity().application, requireParentFragment()) }
    )*/

    fun initViewModel() {
        parentViewModel = ViewModelProvider(requireParentFragment()).get(MainFragmentViewModel::class.java)
    }

    var filters: ArrayList<DrinkFilter> = arrayListOf()
    protected var sortDrinkType: SortDrinkType = SortDrinkType.RECENT

    private val alcoholComparator: AlcoholCocktailComparator = AlcoholCocktailComparator()

    open fun initLiveDataObserver() {
        parentViewModel.filteredCocktailsLiveData.observe(this, Observer { cocktails ->
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

    fun initRecyclerView(view: View, drinks: List<CocktailModel>, recyclerViewId: Int) {
        val recyclerView: RecyclerView = view.findViewById(recyclerViewId)
        cocktailAdapter = DrinkAdapter(viewModel, requireContext())

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = cocktailAdapter

        determineVisibleLayerOnCreate(drinks)

        cocktailAdapter.listData = drinks
    }

    override fun onDestroy() {
        super.onDestroy()
        cocktailAdapter.setLifecycleDestroyed()
    }

    /**
     * If drinks is empty, hide recyclerView and show appropriate textView,
     * else make recyclerView visible.
     *
     * To do this, use methods from ActivityUtil in util package
     *
     * @param drinks data list to check for items
     */
    open fun determineVisibleLayerOnCreate(cocktails: List<CocktailModel?>?) {
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
    open fun determineVisibleLayerOnUpdateData(cocktails: List<CocktailModel?>?) {
        //TO DO
    }

}

