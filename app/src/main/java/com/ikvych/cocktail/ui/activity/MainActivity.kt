package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.os.Bundle
import com.ikvych.cocktail.R
import com.ikvych.cocktail.constant.MAIN_MODEL_TYPE
import com.ikvych.cocktail.data.entity.Drink
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.util.setDbEmptyHistoryVisible
import com.ikvych.cocktail.util.setDbRecyclerViewVisible
import com.ikvych.cocktail.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : RecyclerViewActivity<MainActivityViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel(MainActivityViewModel::class.java)
        initRecyclerView(viewModel.getCurrentData(), R.id.recycler_view, MAIN_MODEL_TYPE)
        initLiveDataObserver()

        fab.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

    }


    override fun determineVisibleLayerOnCreate(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(this@MainActivity)
        } else {
            setDbRecyclerViewVisible(this@MainActivity)
        }
    }

    override fun determineVisibleLayerOnUpdateData(drinks: List<Drink?>?) {
        if (drinks!!.isEmpty()) {
            setDbEmptyHistoryVisible(this@MainActivity)
        } else {
            setDbRecyclerViewVisible(this@MainActivity)
        }
    }
}
