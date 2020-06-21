package com.ikvych.cocktail.util

import android.app.Activity
import android.view.View
import com.ikvych.cocktail.R

fun setSearchEmptyListVisible(activity: Activity) {
    activity.findViewById<View>(R.id.empty_list_id).visibility = View.VISIBLE
    activity.findViewById<View>(R.id.empty_search_id).visibility = View.GONE
    activity.findViewById<View>(R.id.db_recycler_view).visibility = View.GONE
}

fun setSearchRecyclerViewVisible(activity: Activity) {
    activity.findViewById<View>(R.id.empty_list_id).visibility = View.GONE
    activity.findViewById<View>(R.id.empty_search_id).visibility = View.GONE
    activity.findViewById<View>(R.id.db_recycler_view).visibility = View.VISIBLE
}

fun setEmptySearchVisible(activity: Activity) {
    activity.findViewById<View>(R.id.empty_list_id).visibility = View.GONE
    activity.findViewById<View>(R.id.empty_search_id).visibility = View.VISIBLE
    activity.findViewById<View>(R.id.db_recycler_view).visibility = View.GONE
}

fun setDbRecyclerViewVisible(activity: Activity) {
    activity.findViewById<View>(R.id.pager_empty_history).visibility = View.GONE
    activity.findViewById<View>(R.id.db_recycler_view).visibility = View.VISIBLE
}

fun setDbEmptyHistoryVisible(activity: Activity) {
    val view = activity.findViewById<View>(R.id.pager_empty_history)
    view.visibility = View.VISIBLE
    activity.findViewById<View>(R.id.db_recycler_view).visibility = View.GONE
}
