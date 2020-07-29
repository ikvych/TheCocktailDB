package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.adapter.list.DrinkAdapter
import com.ikvych.cocktail.constant.*
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.databinding.ActivitySearchBinding
import com.ikvych.cocktail.listener.DrinkOfferListener
import com.ikvych.cocktail.receiver.DrinkOfferReceiver
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : BaseActivity<SearchActivityViewModel, ActivitySearchBinding>(),
    DrinkOfferListener {

    override var contentLayoutResId: Int = R.layout.activity_search
    override val viewModel: SearchActivityViewModel by viewModels()

    private val drinkOfferReceiver: DrinkOfferReceiver = DrinkOfferReceiver(this)
    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var recyclerView: RecyclerView

    override fun configureView(savedInstanceState: Bundle?) {
        drinkAdapter = DrinkAdapter(viewModel, this)
        initRecyclerView()
        initLiveDataObserver()
        initSearchView()
    }

    override fun configureDataBinding(binding: ActivitySearchBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    private fun initRecyclerView() {
        recyclerView = rv_search_result
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 4)
        }
        recyclerView.adapter = drinkAdapter
    }

    private fun initLiveDataObserver() {
        viewModel.drinkLiveData.observe(this, Observer { drinks ->
            drinkAdapter.listData = drinks!!
        })
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

    private fun initSearchView() {
        atb_search_activity.searchView.isIconifiedByDefault = false
        atb_search_activity.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                atb_search_activity.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if (!newText.isNullOrBlank()) {
                    val searchQuery: String = newText.trim()
                    viewModel.updateDrinksLiveData(searchQuery)
                    return true
                } else {
                    false
                }
            }
        })
    }

    override fun makeOfferOfDrink(intent: Intent) {
        val drinks = viewModel.drinkLiveData.value
        if (drinks.isNullOrEmpty()) {
            return
        }

        val currentDrinkId: Long = intent.getLongExtra(DRINK_ID, -1L)
        val drink: Drink = drinks.shuffled().find { it.getIdDrink() != currentDrinkId } ?: return

        val view: View = findViewById(R.id.rv_search_result)

        Snackbar.make(
            view,
            "${resources.getString(R.string.search_activity_drink_offer_title)} ${drink.getStrDrink()}",
            3500
        )
            .setAction(R.string.toast_action_view) {
                val drinkIntent = Intent(this, DrinkDetailActivity::class.java)
                drinkIntent.putExtra(VIEW_MODEL_TYPE, SEARCH_MODEL_TYPE)
                drinkIntent.putExtra(DRINK, drink)
                startActivity(drinkIntent)
            }.show()
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        } else {
            when (v.id) {
                // відкриває деталізацію коктейлю
                R.id.cv_item_drink -> {
                    val drinkId = v.tag as Long
                    val drink = viewModel.drinkLiveData.value?.first { it.getIdDrink() == drinkId }
                        ?: return
                    val intent = Intent(this, DrinkDetailActivity::class.java)
                    intent.putExtra(SHOULD_SAVE_DRINK, SHOULD_SAVE_DRINK)
                    intent.putExtra(SHOW_DRINK_OFFER_ON_DESTROY, SHOW_DRINK_OFFER_ON_DESTROY)
                    intent.putExtra(DRINK, drink)
                    startActivity(intent)
                }
            }
        }
    }
}
