package com.ikvych.cocktail.auth.navigation

import android.content.Context
import android.content.Intent
import com.ikvych.cocktail.api.AuthStarter
import com.ikvych.cocktail.auth.AuthActivity
import com.ikvych.cocktail.prresentation.navigation.ActivityStarter

class AuthStarterImpl(
    private val context: Context,
    private val activityStarter: ActivityStarter
) : AuthStarter {
    override fun startAuth() {
        activityStarter.run {
            startActivity(Intent(context, AuthActivity::class.java))
        }
    }
}