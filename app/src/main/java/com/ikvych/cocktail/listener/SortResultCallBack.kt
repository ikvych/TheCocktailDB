package com.ikvych.cocktail.listener

import com.ikvych.cocktail.ui.fragment.FilterFragment
import com.ikvych.cocktail.ui.fragment.MainFragment

interface SortResultCallBack {
    val callbacks: HashSet<Unit>
    fun addCallBack() {

    }
    fun removeCallBack() {

    }
}