package com.ikvych.cocktail.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.DrinkFilter
import com.ikvych.cocktail.listener.FilterResultCallBack
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.ui.fragment.FilterFragment
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.ui.fragment.ProfileFragment

class MainActivity : BaseActivity(), FilterFragment.OnFilterResultListener, FilterResultCallBack {

    override val callbacks: HashSet<FilterFragment.OnFilterResultListener> = hashSetOf()

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var mainFragment: MainFragment
    lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bnv_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_main_fragment -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.hide(profileFragment)
                    ft.show(mainFragment)
                    ft.addToBackStack(MainFragment::class.java.simpleName)
                    ft.commit()
                    true
                }
                R.id.menu_profile_fragment -> {
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    ft.hide(mainFragment)
                    ft.show(profileFragment)
                    ft.addToBackStack(ProfileFragment::class.java.simpleName)
                    ft.commit()
                    true
                }
                else -> false
            }
        })

        profileFragment = ProfileFragment.newInstance(R.layout.fragment_profile)
        mainFragment = MainFragment.newInstance(R.layout.fragment_main)
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fcv_main, profileFragment, ProfileFragment::class.java.simpleName)
        fragmentTransaction.hide(profileFragment)
        fragmentTransaction.add(R.id.fcv_main, mainFragment, MainFragment::class.java.simpleName)
        fragmentTransaction.commit()
    }


    override fun onFilterApply(drinkFilters: ArrayList<DrinkFilter>) {
        callbacks.forEach {
            it.onFilterApply(drinkFilters)
        }
    }

    override fun onFilterReset() {
        callbacks.forEach {
            it.onFilterReset()
        }
    }

    override fun addCallBack(listener: FilterFragment.OnFilterResultListener) {
        callbacks.add(listener)
    }

    override fun removeCallBack(listener: FilterFragment.OnFilterResultListener) {
        callbacks.remove(listener)
    }
}

