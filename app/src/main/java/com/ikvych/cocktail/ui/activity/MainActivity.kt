package com.ikvych.cocktail.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.listener.FilterResultCallBack
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.ui.fragment.*
import com.ikvych.cocktail.ui.fragment.HistoryFragment.Companion.newInstance
import com.ikvych.cocktail.widget.custom.ApplicationToolBar
import kotlin.collections.ArrayList


class MainActivity : BaseActivity(), FilterFragment.OnFilterResultListener,
    ProfileFragment.ProfileFragmentListener, FilterResultCallBack {

    override val callbacks: HashSet<FilterFragment.OnFilterResultListener> = hashSetOf()
    lateinit var mainFragment: MainFragment
    lateinit var filterFragment: FilterFragment
    lateinit var filterBtn: ImageButton
    lateinit var indicatorView: TextView
    private lateinit var returnBtn: ImageButton

    lateinit var profileFragment: ProfileFragment
    lateinit var testFragment: TestFragment

    var filters: List<DrinkFilter> = arrayListOf()

    override fun onBackPressed() {
        if (filters.isNotEmpty()) {
            indicatorView.visibility = View.VISIBLE
        } else {
            indicatorView.visibility = View.GONE
        }
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        returnBtn = findViewById<ApplicationToolBar>(R.id.app_toolbar).returnBtn
        returnBtn.setOnClickListener {
            onBackPressed()
        }

        indicatorView = findViewById<ApplicationToolBar>(R.id.app_toolbar).indicatorView

        filterBtn = findViewById<ApplicationToolBar>(R.id.app_toolbar).customBtn
        filterBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_filter))
        filterBtn.setOnClickListener {
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            filterFragment =
                FilterFragment.newInstance(R.layout.fragment_filter, *filters.toTypedArray())
            ft.hide(mainFragment)
            ft.add(R.id.fcv_main, filterFragment)
            ft.addToBackStack(FilterFragment::class.java.name)
            ft.commit()
        }
        filterBtn.setOnLongClickListener {
            if (filters.isNotEmpty()) {
                indicatorView.visibility = View.GONE
                onFilterApply()
            }
            true
        }

        mainFragment = MainFragment.newInstance(R.layout.fragment_main)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.fcv_main, mainFragment)
        ft.commit()

/*        profileFragment = ProfileFragment.newInstance(R.layout.fragment_profile)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.fcv_main, profileFragment)
        ft.commit()*/
    }

    override fun onFilterApply(vararg drinkFilters: DrinkFilter) {
        filters = drinkFilters.toList()
        callbacks.forEach {
            it.onFilterApply(*drinkFilters)
        }
        if (drinkFilters.isNotEmpty()) {
            if (indicatorView.visibility != View.VISIBLE) {
                indicatorView.visibility = View.VISIBLE
            }
        }
        supportFragmentManager.popBackStack()
    }

    override fun onFilterRest() {
        filters = arrayListOf()
        callbacks.forEach {
            it.onFilterRest()
        }
        if (indicatorView.visibility == View.VISIBLE) {
            indicatorView.visibility = View.GONE
        }
        supportFragmentManager.popBackStack()
    }

    override fun startTestFragment() {
        testFragment = TestFragment.newInstance(R.layout.fragment_test, 5, "Ivan Kvych")
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fcv_main, testFragment)
        ft.addToBackStack(TestFragment::class.java.name)
        ft.commit()
    }

    override fun addCallBack(listener: FilterFragment.OnFilterResultListener) {
        callbacks.add(listener)
    }

    override fun removeCallBack(listener: FilterFragment.OnFilterResultListener) {
        callbacks.remove(listener)
    }
}

