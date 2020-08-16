package com.ikvych.cocktail.listener

import android.content.Intent
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ikvych.cocktail.R
import com.ikvych.cocktail.presentation.activity.DrinkDetailActivity
import com.ikvych.cocktail.presentation.activity.MainActivity
import com.ikvych.cocktail.presentation.activity.ProfileActivity
import com.ikvych.cocktail.presentation.dialog.regular.ErrorDialogFragment
import com.ikvych.cocktail.presentation.dialog.regular.RatingAppDialogFragment
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
                    if (it.cocktailId != null) {
                        viewModel.getCocktailByIdLiveDataAndSave(it.cocktailId)
                            .observeNotNullOnce { cocktail ->
                                activity.startActivity(
                                    Intent(
                                        activity,
                                        DrinkDetailActivity::class.java
                                    ).apply {
                                        putExtra(COCKTAIL_ID, cocktail.id)
                                    })
                            }
                    }
                    viewModel.deleteNotification()
                }
                NotificationType.NOTIFICATION_TYPE_RATE_APP -> {
                    RatingAppDialogFragment.newInstance {
                        titleText = activity.getString(R.string.rating_bar_dialog_title)
                        leftButtonText = activity.getString(R.string.all_ok_button)
                        isCloseButtonVisible = true
                    }.show(
                        activity.supportFragmentManager,
                        RatingAppDialogFragment::class.java.simpleName
                    )
                    viewModel.deleteNotification()
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