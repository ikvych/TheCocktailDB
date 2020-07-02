package com.ikvych.cocktail.util

import android.app.Activity
import android.view.View
import com.ikvych.cocktail.R

fun setSearchEmptyListVisible(activity: Activity) {
    activity.findViewById<View>(R.id.tv_begin_search).visibility = View.VISIBLE
    activity.findViewById<View>(R.id.tv_empty_search_result).visibility = View.GONE
    activity.findViewById<View>(R.id.rv_search_result).visibility = View.GONE
}

fun setSearchRecyclerViewVisible(activity: Activity) {
    activity.findViewById<View>(R.id.tv_begin_search).visibility = View.GONE
    activity.findViewById<View>(R.id.tv_empty_search_result).visibility = View.GONE
    activity.findViewById<View>(R.id.rv_search_result).visibility = View.VISIBLE
}

fun setEmptySearchVisible(activity: Activity) {
    activity.findViewById<View>(R.id.tv_begin_search).visibility = View.GONE
    activity.findViewById<View>(R.id.tv_empty_search_result).visibility = View.VISIBLE
    activity.findViewById<View>(R.id.rv_search_result).visibility = View.GONE
}

fun setDbRecyclerViewVisible(view: View) {
    view.findViewById<View>(R.id.tv_empty_history).visibility = View.GONE
    view.findViewById<View>(R.id.rv_search_result).visibility = View.VISIBLE
}

fun setDbEmptyHistoryVisible(view: View) {
    view.findViewById<View>(R.id.tv_empty_history).visibility = View.VISIBLE
    view.findViewById<View>(R.id.rv_search_result).visibility = View.GONE
}
