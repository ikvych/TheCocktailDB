package com.ikvych.cocktail.main.listener

import android.content.Intent
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ikvych.cocktail.main.R
import com.ikvych.cocktail.prresentation.dialog.regular.RatingAppDialogFragment
import com.ikvych.cocktail.prresentation.extension.observeNotNullOnce
import com.ikvych.cocktail.prresentation.util.COCKTAIL_ID

class MainActivityLifecycleObserver(
/*    private val activity: com.ikvych.cocktail.main.MainActivity,
    private val viewModel: com.ikvych.cocktail.firebase.firebase.NotificationViewModel*/
) : LifecycleObserver {

/*    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onStart() {
        viewModel.getNotificationLiveData().observeNotNullOnce {
            when (it.type) {
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_MAIN -> {
                    viewModel.deleteNotification()
                }
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_PROFILE -> {
                    startProfileActivity()
                    viewModel.deleteNotification()
                }
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_PROFILE_EDIT -> {
                    startProfileActivity()
                }
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_COCKTAIL_DETAIL -> {
                    if (it.cocktailId != null) {
                        viewModel.getCocktailByIdLiveDataAndSave(it.cocktailId)
                            .observeNotNullOnce { cocktail ->
                                activity.startActivity(
                                    Intent(
                                        activity,
                                        com.ikvych.cocktail.detail.DrinkDetailActivity::class.java
                                    ).apply {
                                        putExtra(COCKTAIL_ID, cocktail.id)
                                    })
                            }
                    }
                    viewModel.deleteNotification()
                }
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_RATE_APP -> {
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
                com.ikvych.cocktail.firebase.firebase.model.NotificationType.NOTIFICATION_TYPE_UNDEFINED -> {

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
        activity.startActivity(Intent(activity, com.ikvych.cocktail.profile.ProfileActivity::class.java))
    }*/
}