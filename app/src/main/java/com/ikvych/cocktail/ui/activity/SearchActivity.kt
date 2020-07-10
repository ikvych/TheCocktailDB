package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.DrinkAdapter
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.data.db.model.Drink
import com.ikvych.cocktail.databinding.ActivitySearchBinding
import com.ikvych.cocktail.listener.DrinkOfferListener
import com.ikvych.cocktail.receiver.DrinkOfferReceiver
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.util.setEmptySearchVisible
import com.ikvych.cocktail.util.setSearchEmptyListVisible
import com.ikvych.cocktail.util.setSearchRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel
import com.ikvych.cocktail.widget.custom.ApplicationToolBar
import kotlinx.android.synthetic.main.activity_search.*
import kotlin.reflect.KClass


class SearchActivity : BaseActivity<SearchActivityViewModel, ActivitySearchBinding>(), DrinkOfferListener {

    override var contentLayoutResId: Int = R.layout.activity_search

    override val viewModelClass: KClass<SearchActivityViewModel>
        get() = SearchActivityViewModel::class

    private lateinit var toolbarSearchView: SearchView
    private val drinkOfferReceiver: DrinkOfferReceiver = DrinkOfferReceiver(this)
    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var recyclerView: RecyclerView

    override fun configureView(savedInstanceState: Bundle?) {
        drinkAdapter = DrinkAdapter(viewModel,this)
        initRecyclerView()
        initLiveDataObserver()
        initSearchView()
    }

    private fun initLiveDataObserver() {
        viewModel.drinkLiveData.observe(this, Observer { drinks ->
            drinkAdapter.listData = drinks
            determineVisibleLayerOnUpdateData(drinks)
        })

        viewModel.startDrinkDetailsLiveData.observe(this, Observer {
            if (it != null) {
                val intent = Intent(this, DrinkDetailActivity::class.java)
                intent.putExtra(VIEW_MODEL_TYPE, SEARCH_MODEL_TYPE)
                intent.putExtra(DRINK, it)
                startActivity(intent)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView = rv_search_result
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = drinkAdapter

        determineVisibleLayerOnCreate(arrayListOf())

        drinkAdapter.listData = arrayListOf()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(ACTION_SHOW_DRINK_OFFER)
        }
        registerReceiver(drinkOfferReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(drinkOfferReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        drinkAdapter.setLifecycleDestroyed()
    }

    private fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setSearchEmptyListVisible(this@SearchActivity)
        } else {
            setSearchRecyclerViewVisible(this@SearchActivity)
        }
    }

    private fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setEmptySearchVisible(this@SearchActivity)
        } else {
            setSearchRecyclerViewVisible(this@SearchActivity)
        }
    }

    private fun initSearchView() {
        toolbarSearchView = findViewById<ApplicationToolBar>(R.id.atb_search_activity).searchView
        toolbarSearchView.isIconifiedByDefault = false
        toolbarSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                toolbarSearchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchQuery: String = newText?.trim() ?: ""
                viewModel.updateDrinksLiveData(searchQuery)
                return true
            }

        })
    }

    override fun update(intent: Intent) {
        val drinks = viewModel.drinkLiveData.value
        if (drinks.isNullOrEmpty()) {
            return
        }

        val currentDrinkId: Long = intent.getLongExtra(DRINK_ID, -1L)
        val drink: Drink = drinks.shuffled().find { it.getIdDrink() != currentDrinkId } ?: return

        val view: View = findViewById(R.id.rv_search_result)

        Snackbar.make(view, "Як щодо - ${drink.getStrDrink()}", 3500)
            .setAction(R.string.toast_action_view) {
                val drinkIntent = Intent(this, DrinkDetailActivity::class.java)
                drinkIntent.putExtra(VIEW_MODEL_TYPE, SEARCH_MODEL_TYPE)
                drinkIntent.putExtra(DRINK, drink)
                startActivity(drinkIntent)
            }.show()
    }

    override fun onClick(v: View?) {
        val view = v?.findViewById<TextView>(R.id.tv_drink_name)
        val drinkName = view?.text ?: ""
        val drink: Drink? =
            drinkAdapter.listData.find { drink -> drink.getStrDrink() == drinkName }

        if (drink != null) {
            val intent = Intent(this, DrinkDetailActivity::class.java)
            intent.putExtra(VIEW_MODEL_TYPE, SEARCH_MODEL_TYPE)
            intent.putExtra(DRINK, drink)
            startActivity(intent)
        }
    }
}
