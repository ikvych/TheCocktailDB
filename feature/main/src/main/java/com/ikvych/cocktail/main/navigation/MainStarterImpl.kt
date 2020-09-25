package com.ikvych.cocktail.main.navigation

import android.content.Context
import android.content.Intent
import com.ikvych.cocktail.api.MainStarter
import com.ikvych.cocktail.main.MainActivity
import com.ikvych.cocktail.prresentation.navigation.ActivityStarter

class MainStarterImpl(
    private val context: Context,
    private val activityStarter: ActivityStarter
) : MainStarter {
    override fun startMain() {
        activityStarter.run {
            startActivity(Intent(context, MainActivity::class.java))
        }
    }
}