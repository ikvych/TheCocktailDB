package com.ikvych.cocktail.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.ui.fragment.FilterFragment
import com.ikvych.cocktail.ui.fragment.MainFragment
import com.ikvych.cocktail.ui.fragment.ProfileFragment
import com.ikvych.cocktail.ui.fragment.TestFragment
import com.ikvych.cocktail.widget.custom.ApplicationToolBar


class MainActivity : BaseActivity(), FilterFragment.FilterFragmentListener, ProfileFragment.ProfileFragmentListener {

    lateinit var mainFragment: MainFragment
    lateinit var filterFragment: FilterFragment
    lateinit var filterBtn: ImageButton
    private lateinit var returnBtn: ImageButton

    lateinit var profileFragment: ProfileFragment
    lateinit var testFragment: TestFragment

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

/*        mainFragment = MainFragment.newInstance(R.layout.fragment_main)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.fcv_main, mainFragment)
        ft.commit()*/

        profileFragment = ProfileFragment.newInstance(R.layout.fragment_profile)
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.add(R.id.fcv_main, profileFragment)
        ft.commit()
    }

    override fun onFilterApply(alcoholDrinkFilter: AlcoholDrinkFilter) {
        mainFragment.filterData(alcoholDrinkFilter)
        supportFragmentManager.popBackStack()
    }

    override fun onFilterRest() {

    }

    override fun startTestFragment() {
        testFragment = TestFragment.newInstance(R.layout.fragment_test, 5, "Ivan Kvych")
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fcv_main, testFragment)
        ft.addToBackStack(TestFragment::class.java.name)
        ft.commit()
    }
}

