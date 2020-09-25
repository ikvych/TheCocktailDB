package com.ikvych.cocktail.profile.navigation

import android.content.Context
import android.content.Intent
import com.ikvych.cocktail.api.ProfileStarter
import com.ikvych.cocktail.profile.ProfileActivity
import com.ikvych.cocktail.prresentation.navigation.ActivityStarter

class ProfileStarterImpl(
    private val context: Context,
    private val activityStarter: ActivityStarter
) : ProfileStarter{
    override fun startProfile() {
        activityStarter.run {
            startActivity(Intent(context, ProfileActivity::class.java))
        }
    }
}