package com.ikvych.cocktail.ui.activity

import android.os.Bundle
import android.widget.SearchView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.SEARCH_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.util.setEmptySearchVisible
import com.ikvych.cocktail.util.setSearchEmptyListVisible
import com.ikvych.cocktail.util.setSearchRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel


class SearchActivity : BaseActivity<SearchActivityViewModel>() {

    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViewModel(SearchActivityViewModel::class.java)
        initRecyclerView(viewModel.getCurrentData(), R.id.recycler_view, SEARCH_MODEL_TYPE)
        initLiveDataObserver()
        initSearchView()

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
        searchView = findViewById(R.id.search_query)
        searchView.isIconifiedByDefault = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchQuery: String = query?.trim() ?: ""
                viewModel.updateDrinksLiveData(searchQuery)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }


}
