package com.ikvych.cocktail.listener

import android.content.Intent
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ikvych.cocktail.presentation.activity.DrinkDetailActivity
import com.ikvych.cocktail.presentation.activity.MainActivity
import com.ikvych.cocktail.presentation.activity.ProfileActivity
import com.ikvych.cocktail.presentation.extension.observeNotNullOnce
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.presentation.model.notification.NotificationType
import com.ikvych.cocktail.util.COCKTAIL_ID
import com.ikvych.cocktail.viewmodel.notification.NotificationViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityLifecycleObserver(
    private val activity: MainActivity,
    private val viewModel: NotificationViewModel
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onStart() {
        viewModel.getNotificationLiveData().observeNotNullOnce {
            when (it.type) {
                NotificationType.NOTIFICATION_TYPE_MAIN -> {
                    viewModel.deleteNotification()
                }
                NotificationType.NOTIFICATION_TYPE_PROFILE -> {
                    startProfileActivity()
                    viewModel.deleteNotification()
                }
                NotificationType.NOTIFICATION_TYPE_PROFILE_EDIT -> {
                    startProfileActivity()
                }
                NotificationType.NOTIFICATION_TYPE_COCKTAIL_DETAIL -> {
                    val intent =
                        Intent(activity, DrinkDetailActivity::class.java)
                    intent.putExtra(COCKTAIL_ID, it.cocktailId)
                    activity.startActivity(intent)
                    viewModel.deleteNotification()
                }
                NotificationType.NOTIFICATION_TYPE_RATE_APP -> {

                }
                NotificationType.NOTIFICATION_TYPE_UNDEFINED -> {

                }
            }
        }
    }

    private fun startProfileActivity() {
        val ft: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        ft.hide(activity.supportFragmentManager.fragments[1])
        ft.show(activity.supportFragmentManager.fragments[0])
        ft.setPrimaryNavigationFragment(activity.supportFragmentManager.fragments[0])
        ft.commit()
        activity.bnv_main.menu.getItem(1).isChecked = true
        activity.startActivity(Intent(activity, ProfileActivity::class.java))
    }
}