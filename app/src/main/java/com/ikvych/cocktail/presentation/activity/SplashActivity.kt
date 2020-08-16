package com.ikvych.cocktail.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivitySplashBinding
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.presentation.extension.viewModels
import com.ikvych.cocktail.presentation.model.notification.NotificationModel
import com.ikvych.cocktail.service.firebase.AppFirebaseMessagingService.Companion.EXTRA_NOTIFICATION
import com.ikvych.cocktail.viewmodel.user.ProfileActivityViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import com.ikvych.cocktail.viewmodel.notification.NotificationViewModel
import java.lang.NullPointerException
import kotlin.reflect.KClass

class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_splash
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    private val profileViewModel: ProfileActivityViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun configureView(savedInstanceState: Bundle?) {

        if (intent.hasExtra(EXTRA_NOTIFICATION)) {
            val notificationModel = intent.getParcelableExtra<NotificationModel>(EXTRA_NOTIFICATION)
            if (notificationModel != null) {
                notificationViewModel.saveNotification(notificationModel)
            }
        }

        profileViewModel.checkForUser()
        profileViewModel.isUserPresentLiveData.observeOnce {
            if (it) {
                profileViewModel.refreshUser()
                Handler().postDelayed({
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 1000)
            } else {
                Handler().postDelayed({
                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 1000)
            }
        }
    }
}