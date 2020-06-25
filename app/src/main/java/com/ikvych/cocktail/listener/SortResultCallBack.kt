package com.ikvych.cocktail.listener

import com.ikvych.cocktail.ui.fragment.FilterFragment
import com.ikvych.cocktail.ui.fragment.MainFragment

interface SortResultCallBack {
    val callbacks: HashSet<MainFragment.OnSortResultListener>
    fun addCallBack(listener: MainFragment.OnSortResultListener)
    fun removeCallBack(listener: MainFragment.OnSortResultListener)
}