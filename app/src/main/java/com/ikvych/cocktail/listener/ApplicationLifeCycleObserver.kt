package com.ikvych.cocktail.listener

import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


const val CURRENT_TIME_MILLIS = "CURRENT_TIME_MILLIS"
const val AWAITING_TIME = 5000L

class ApplicationLifeCycleObserver(
    val listener: OnLifecycleObserverListener,
    private val sharedPreferences: SharedPreferences
) : LifecycleObserver {

    interface OnLifecycleObserverListener{
        fun shouldShowDrinkOfTheDay()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        if (sharedPreferences.contains(CURRENT_TIME_MILLIS)) {
            val savedTime = sharedPreferences.getLong(CURRENT_TIME_MILLIS, -1)
            val currentTime = System.currentTimeMillis()
            if ((currentTime - savedTime) >= AWAITING_TIME) {
                listener.shouldShowDrinkOfTheDay()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        val editor = sharedPreferences.edit()
        editor.putLong(CURRENT_TIME_MILLIS, System.currentTimeMillis())
        editor.apply()
    }

}