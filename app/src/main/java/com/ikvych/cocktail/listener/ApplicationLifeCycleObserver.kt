package com.ikvych.cocktail.listener

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.CURRENT_TIME_MILLIS
import com.ikvych.cocktail.ui.dialog.ResumeAppBottomSheetDialogFragment

class ApplicationLifeCycleObserver(
    val listener: OnLifecycleObserverListener,
    val context: Context,
    private val sharedPreferences: SharedPreferences
) : LifecycleObserver {

    interface OnLifecycleObserverListener{
        fun shouldShowDialog()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onActivityStart() {
        if (sharedPreferences.contains(CURRENT_TIME_MILLIS)) {
            val savedTime = sharedPreferences.getLong(CURRENT_TIME_MILLIS, -1)
            val currentTime = System.currentTimeMillis()
            if ((currentTime - savedTime) <= 10000L) {
                listener.shouldShowDialog()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onActivityStop() {
        val editor = sharedPreferences.edit()
        editor.putLong(CURRENT_TIME_MILLIS, System.currentTimeMillis())
        editor.apply()
    }

}