package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.listener.DrinkOfferListener
import com.ikvych.cocktail.receiver.DrinkOfferReceiver
import com.ikvych.cocktail.util.setEmptySearchVisible
import com.ikvych.cocktail.util.setSearchEmptyListVisible
import com.ikvych.cocktail.util.setSearchRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel
import com.ikvych.cocktail.widget.custom.ApplicationToolBar


class SearchActivity : RecyclerViewActivity<SearchActivityViewModel>(), DrinkOfferListener {

    lateinit var toolbarSearchView: SearchView
    private val drinkOfferReceiver: DrinkOfferReceiver = DrinkOfferReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViewModel(SearchActivityViewModel::class.java)
        initRecyclerView(viewModel.getCurrentData(), R.id.db_recycler_view)
        initLiveDataObserver()
        initSearchView()
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

    override fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setSearchEmptyListVisible(this@SearchActivity)
        } else {
            setSearchRecyclerViewVisible(this@SearchActivity)
        }
    }

    override fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setEmptySearchVisible(this@SearchActivity)
        } else {
            setSearchRecyclerViewVisible(this@SearchActivity)
        }
    }

    private fun initSearchView() {
        toolbarSearchView = findViewById<ApplicationToolBar>(R.id.app_toolbar).searchView
        toolbarSearchView.isIconifiedByDefault = false
        toolbarSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery: String = query?.trim() ?: ""
                viewModel.updateDrinksLiveData(searchQuery)
                toolbarSearchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun update(intent: Intent) {
        val drinks = viewModel.getCurrentData()
        if (drinks.isEmpty()) {
            return
        }

        val currentDrinkId: Long = intent.getLongExtra(DRINK_ID, -1L)
        val drink: Drink = drinks.shuffled().find { it.getIdDrink() != currentDrinkId } ?: return

        val view: View = findViewById(R.id.db_recycler_view)

        Snackbar.make(view, "Як щодо - ${drink.getStrDrink()}", 3500)
            .setAction(R.string.toast_action_view) {
                val drinkIntent = Intent(this, DrinkDetailActivity::class.java)
                drinkIntent.putExtra(VIEW_MODEL_TYPE, SEARCH_MODEL_TYPE)
                drinkIntent.putExtra(DRINK, drink)
                startActivity(drinkIntent)
            }.show()
    }

    override fun onClick(v: View?) {
        val view = v?.findViewById<TextView>(R.id.drinkName)
        val drinkName = view?.text ?: ""
        val drink: Drink? =
            drinkAdapter.drinkList.find { drink -> drink.getStrDrink() == drinkName }

        if (drink != null) {
            val intent = Intent(this, DrinkDetailActivity::class.java)
            intent.putExtra(VIEW_MODEL_TYPE, SEARCH_MODEL_TYPE)
            intent.putExtra(DRINK, drink)
            startActivity(intent)
        }
    }
}
