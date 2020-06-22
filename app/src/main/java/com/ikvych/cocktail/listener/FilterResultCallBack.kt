package com.ikvych.cocktail.listener

import com.ikvych.cocktail.ui.fragment.FilterFragment

interface FilterResultCallBack {
    val callbacks: HashSet<FilterFragment.OnFilterResultListener>
    fun addCallBack(listener: FilterFragment.OnFilterResultListener)
    fun removeCallBack(listener: FilterFragment.OnFilterResultListener)
}