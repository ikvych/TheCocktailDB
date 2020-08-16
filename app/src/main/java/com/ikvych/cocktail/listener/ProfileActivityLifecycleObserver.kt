package com.ikvych.cocktail.listener

import android.content.Intent
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ikvych.cocktail.R
import com.ikvych.cocktail.presentation.activity.MainActivity
import com.ikvych.cocktail.presentation.activity.ProfileActivity
import com.ikvych.cocktail.presentation.extension.observeNotNullOnce
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.presentation.fragment.ProfileMainInfoFragment
import com.ikvych.cocktail.presentation.model.notification.NotificationType
import com.ikvych.cocktail.viewmodel.notification.NotificationViewModel
import kotlinx.android.synthetic.main.activity_main.*

class ProfileActivityLifecycleObserver(
    private val activity: ProfileActivity,
    private val viewModel: NotificationViewModel
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onStart() {
        viewModel.getNotificationLiveData().observeNotNullOnce {
            when (it.type) {
                NotificationType.NOTIFICATION_TYPE_PROFILE_EDIT -> {
                    val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
                    fragmentTransaction.add(
                        R.id.fcv_profile,
                        ProfileMainInfoFragment.newInstance(),
                        ProfileMainInfoFragment::class.java.simpleName
                    )
                    fragmentTransaction.addToBackStack(ProfileMainInfoFragment::class.java.name)
                    fragmentTransaction.commit()
                    viewModel.deleteNotification()
                }
                NotificationType.NOTIFICATION_TYPE_UNDEFINED -> {

                }
                else -> {}
            }
        }
    }

}