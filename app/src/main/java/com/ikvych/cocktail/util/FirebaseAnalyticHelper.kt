package com.ikvych.cocktail.util

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.ikvych.cocktail.data.db.impl.DrinkDataBase

class FirebaseAnalyticHelper private constructor(
    private val context: Context
) {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logEvent(event: String, value: Bundle) {
        firebaseAnalytics.logEvent(
            event,
            value
        )
    }

    companion object {

        private var instance: FirebaseAnalyticHelper? = null

        @Synchronized
        fun getInstance(context: Context): FirebaseAnalyticHelper {
            if (instance == null) {
                instance = FirebaseAnalyticHelper(context)
            }
            return instance!!
        }
    }
}