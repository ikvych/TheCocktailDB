package com.ikvych.cocktail.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ApplicationLifeCycleObserver(
    val listener: OnLifecycleObserverListener
) : LifecycleObserver {

    interface OnLifecycleObserverListener{
        fun actionOnStart()
        fun actionOnStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onActivityStart() {
        listener.actionOnStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onActivityStop() {
        listener.actionOnStop()
    }

}