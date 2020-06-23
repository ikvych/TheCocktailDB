package com.ikvych.cocktail.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.fragment.HistoryFragment

class DrinkPagerAdapter(
    private val fragments: ArrayList<Fragment> = arrayListOf(),
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}