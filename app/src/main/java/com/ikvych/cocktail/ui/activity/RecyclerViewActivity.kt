package com.ikvych.cocktail.ui.activity

import android.content.res.Configuration
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikvych.cocktail.adapter.list.DrinkAdapter
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

abstract class RecyclerViewActivity<T : BaseViewModel> : BaseActivity(), View.OnClickListener, View.OnLongClickListener {
    protected lateinit var drinkAdapter: DrinkAdapter
    lateinit var viewModel : T

    fun initViewModel(viewModelClass: Class<T>) {
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }

    fun initLiveDataObserver() {
        viewModel.getLiveData()!!.observe(this, Observer { drinks ->
            drinkAdapter.drinkList = drinks
            determineVisibleLayerOnUpdateData(drinks)
        })
    }

    fun initRecyclerView(drinks: List<Drink>, recyclerViewId: Int) {
        val recyclerView: RecyclerView = findViewById(recyclerViewId)
        drinkAdapter = DrinkAdapter(this)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = drinkAdapter

        determineVisibleLayerOnCreate(drinks)

        drinkAdapter.drinkList = drinks
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

    override fun onClick(v: View?) {

    }

    override fun onLongClick(v: View?): Boolean {
        return false
    }
}