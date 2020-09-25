package com.ikvych.cocktail.prresentation.listener

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
class ProfileActivityLifecycleObserver(/*
    private val activity: com.ikvych.cocktail.profile.ProfileActivity,
    private val viewModel: com.ikvych.cocktail.firebase.firebase.NotificationViewModel*/
) : LifecycleObserver {

/*    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onStart() {
        viewModel.getNotificationLiveData().observeNotNullOnce {
            when (it.type) {
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_PROFILE_EDIT -> {
                    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
                    fragmentTransaction.add(
                        R.id.fcv_profile,
                        com.ikvych.cocktail.profile.ProfileMainInfoFragment.newInstance(),
                        com.ikvych.cocktail.profile.ProfileMainInfoFragment::class.java.simpleName
                    )
                    fragmentTransaction.addToBackStack(com.ikvych.cocktail.profile.ProfileMainInfoFragment::class.java.name)
                    fragmentTransaction.commit()
                    viewModel.deleteNotification()
                }
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_UNDEFINED -> {

                }
                else -> {}
            }
        }
    }*/

}