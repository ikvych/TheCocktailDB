package com.ikvych.cocktail.search.navigation

import android.content.Context
import android.content.Intent
import com.ikvych.cocktail.api.SearchStarter
import com.ikvych.cocktail.prresentation.navigation.ActivityStarter
import com.ikvych.cocktail.search.SearchActivity

class SearchStarterImpl(
    private val context: Context,
    private val activityStarter: ActivityStarter
) : SearchStarter{
    override fun startSearch() {
        activityStarter.run {
            startActivity(Intent(context, SearchActivity::class.java))
        }
    }
}