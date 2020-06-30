package com.ikvych.cocktail.listener

import com.ikvych.cocktail.ui.fragment.FilterFragment

interface FilterResultCallBack {
    val callbacks: HashSet<Unit>
    fun addCallBack() {

    }
    fun removeCallBack() {

    }
}