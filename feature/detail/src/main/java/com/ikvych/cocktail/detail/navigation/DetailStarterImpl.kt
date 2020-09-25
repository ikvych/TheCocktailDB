package com.ikvych.cocktail.detail.navigation

import android.content.Context
import android.content.Intent
import com.ikvych.cocktail.api.DetailStarter
import com.ikvych.cocktail.detail.DrinkDetailActivity
import com.ikvych.cocktail.prresentation.model.cocktail.CocktailModel
import com.ikvych.cocktail.prresentation.navigation.ActivityStarter

class DetailStarterImpl(
    private val context: Context,
    private val activityStarter: ActivityStarter
) : DetailStarter {

    override fun startDetailById(extraKey: String, cocktailId: Long, vararg extras: Pair<String, String>) {
        activityStarter.run {
            startActivity(Intent(context, DrinkDetailActivity::class.java).apply {
                putExtra(extraKey, cocktailId)
                extras.forEach {
                    putExtra(it.first, it.second)
                }
            })
        }
    }

    override fun startDetailWithModel(
        extraKey: String,
        cocktail: Any,
        vararg extras: Pair<String, String>
    ) {
        activityStarter.run {
            startActivity(Intent(context, DrinkDetailActivity::class.java).apply {
                putExtra(extraKey, cocktail as CocktailModel)
                extras.forEach {
                    putExtra(it.first, it.second)
                }
            })
        }
    }
}