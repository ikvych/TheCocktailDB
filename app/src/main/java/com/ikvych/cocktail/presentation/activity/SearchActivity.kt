package com.ikvych.cocktail.presentation.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivitySearchBinding
import com.ikvych.cocktail.listener.DrinkOfferListener
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.adapter.list.CocktailAdapter
import com.ikvych.cocktail.presentation.adapter.list.ItemViewLook
import com.ikvych.cocktail.presentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.receiver.DrinkOfferReceiver
import com.ikvych.cocktail.util.*
import com.ikvych.cocktail.util.widget.custom.ApplicationToolBar
import com.ikvych.cocktail.viewmodel.cocktail.SearchActivityViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlin.reflect.KClass


class SearchActivity : BaseActivity<SearchActivityViewModel, ActivitySearchBinding>(),
    DrinkOfferListener {

    override var contentLayoutResId: Int = R.layout.activity_search

    override val viewModelClass: KClass<SearchActivityViewModel>
        get() = SearchActivityViewModel::class

    private lateinit var toolbarSearchView: SearchView
    private val drinkOfferReceiver: DrinkOfferReceiver = DrinkOfferReceiver(this)
    private lateinit var drinkAdapter: CocktailAdapter
    private lateinit var recyclerView: RecyclerView

    override fun configureView(savedInstanceState: Bundle?) {
        recyclerView = rv_search_result
        drinkAdapter = CocktailAdapter(
            viewModel,
            this,
            ItemViewLook.CARD_ITEM,
            recyclerView,
            4
        )
        recyclerView.adapter = drinkAdapter
        initLiveDataObserver()
        initSearchView()
    }

    override fun configureDataBinding(binding: ActivitySearchBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    private fun initLiveDataObserver() {
        viewModel.cocktailLiveData.observe(this, Observer { cocktails ->
            drinkAdapter.updateListData(cocktails)
        })
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(ACTION_SHOW_COCKTAIL_OFFER)
        }
        registerReceiver(drinkOfferReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(drinkOfferReceiver)
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
                return if (!newText.isNullOrBlank()) {
                    val searchQuery: String = newText.trim()
                    viewModel.searchQueryLiveData.value = searchQuery
                    return true
                } else {
                    false
                }
            }

        })
    }

    override fun makeOfferOfDrink(intent: Intent) {
        val cocktails = viewModel.cocktailLiveData.value
        if (cocktails.isNullOrEmpty()) {
            return
        }

        val currentCocktailId: Long = intent.getLongExtra(COCKTAIL_ID, -1L)
        val cocktail: CocktailModel =
            cocktails.shuffled().find { it.id != currentCocktailId } ?: return

        Snackbar.make(
            rv_search_result,
            "${resources.getString(R.string.search_activity_drink_offer_title)} - ${cocktail.names.defaultName}",
            3500
        )
            .setAction(R.string.toast_action_view) {
                val drinkIntent = Intent(this, DrinkDetailActivity::class.java)
                drinkIntent.putExtra(COCKTAIL_ID, cocktail.id)
                startActivity(drinkIntent)
            }.show()
    }

    override fun onClick(v: View?) {
        if (v == null) {
            return
        } else {
            when (v.id) {
                // відкриває деталізацію коктейлю
                //v is CardView якій я попередньо прописав tag як id напою
                R.id.cv_item_drink -> {
                    val cocktailId = v.tag as Long
                    val cocktail =
                        drinkAdapter.getListData().find { cocktail -> (cocktail as CocktailModel).id == cocktailId } as? CocktailModel
                            ?: return
                    val intent = Intent(this, DrinkDetailActivity::class.java)
                    intent.putExtra(
                        SHOULD_SAVE_COCKTAIL,
                        SHOULD_SAVE_COCKTAIL
                    )
                    intent.putExtra(
                        SHOW_COCKTAIL_OFFER_ON_DESTROY,
                        SHOW_COCKTAIL_OFFER_ON_DESTROY
                    )
                    intent.putExtra(COCKTAIL, cocktail)
                    startActivity(intent)
                }
            }
        }
    }
}
