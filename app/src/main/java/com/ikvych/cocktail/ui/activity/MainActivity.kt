package com.ikvych.cocktail.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.ui.fragment.FilterFragment
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.widget.custom.ApplicationToolBar


class MainActivity : BaseActivity(), FilterFragment.OnFilterFragmentListener {

    lateinit var mainFragment: MainFragment
    lateinit var filterFragment: FilterFragment
    lateinit var filterBtn: ImageButton
    private lateinit var returnBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        returnBtn = findViewById<ApplicationToolBar>(R.id.app_toolbar).returnBtn
        returnBtn.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }

        filterBtn = findViewById<ApplicationToolBar>(R.id.app_toolbar).customBtn
        filterBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_filter))
        filterBtn.setOnClickListener {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            filterFragment = FilterFragment.newInstance(R.layout.fragment_filter)
            ft.hide(mainFragment)
            ft.add(R.id.fcv_main, filterFragment)
            ft.addToBackStack(FilterFragment::class.java.name)
            ft.commit()
        }

        mainFragment = MainFragment.newInstance(R.layout.fragment_main)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.fcv_main, mainFragment)
        ft.commit()
    }

    override fun onFilterApply(alcoholDrinkFilter: AlcoholDrinkFilter) {
        mainFragment.filterData(alcoholDrinkFilter)
        supportFragmentManager.popBackStack()
    }

    override fun onFilterRest() {
    }

}

