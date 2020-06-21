package com.ikvych.cocktail.adapter.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.fragment.HistoryFragment

class DrinkPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return HistoryFragment.newInstance(R.layout.fragment_history)
    }


}