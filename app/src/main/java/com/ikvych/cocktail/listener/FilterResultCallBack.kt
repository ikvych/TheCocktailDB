package com.ikvych.cocktail.listener

import com.ikvych.cocktail.ui.fragment.FilterFragment

interface FilterResultCallBack {
    val callbacks: HashSet<FilterFragment.OnFilterResultListener>
    fun addCallBack(listener: FilterFragment.OnFilterResultListener) {
        callbacks.add(listener)
    }
    fun removeCallBack(listener: FilterFragment.OnFilterResultListener) {
        callbacks.remove(listener)
    }
}