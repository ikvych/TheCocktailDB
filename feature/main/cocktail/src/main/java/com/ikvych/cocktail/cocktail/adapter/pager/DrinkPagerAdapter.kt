package com.ikvych.cocktail.cocktail.adapter.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ikvych.cocktail.cocktail.FavoriteFragment
import com.ikvych.cocktail.cocktail.HistoryFragment
import com.ikvych.cocktail.prresentation.util.enumeration.Page

class DrinkPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            Page.HISTORY.ordinal -> com.ikvych.cocktail.cocktail.HistoryFragment.newInstance()
            Page.FAVORITE.ordinal -> com.ikvych.cocktail.cocktail.FavoriteFragment.newInstance()
            else -> throw IllegalStateException("Unknown page")
        }
    }
}