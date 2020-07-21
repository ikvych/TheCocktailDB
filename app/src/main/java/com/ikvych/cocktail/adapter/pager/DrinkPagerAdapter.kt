package com.ikvych.cocktail.adapter.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ikvych.cocktail.ui.fragment.FavoriteFragment
import com.ikvych.cocktail.ui.fragment.HistoryFragment
import com.ikvych.cocktail.util.Page

class DrinkPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            Page.HISTORY.ordinal -> HistoryFragment.newInstance()
            Page.FAVORITE.ordinal -> FavoriteFragment.newInstance()
            else -> throw IllegalStateException("Unknown page")
        }
    }
}