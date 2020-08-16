package com.ikvych.cocktail.presentation.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivitySplashBinding
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.presentation.extension.viewModels
import com.ikvych.cocktail.presentation.model.notification.NotificationModel
import com.ikvych.cocktail.presentation.model.notification.NotificationType
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
        initProcessingOfDynamicLink()
        initProcessingOfPushNotification()

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

    private fun initProcessingOfDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    if (deepLink != null) {
                        val type = deepLink.getQueryParameter("type")
                            ?: NotificationType.NOTIFICATION_TYPE_UNDEFINED.key
                        val cocktailId = deepLink.getQueryParameter("id")?.toLong() ?: -1L

                        val notificationType =
                            NotificationType.values().firstOrNull { it.key == type }
                                ?: NotificationType.NOTIFICATION_TYPE_UNDEFINED

                        notificationViewModel.saveNotification(
                            NotificationModel(
                                type = notificationType,
                                cocktailId = cocktailId
                            )
                        )
                    }
                }
            }
            .addOnFailureListener(this) { e ->
                Log.d(
                    "DYNAMIC_LINK",
                    "getDynamicLink:onFailure",
                    e
                )
            }
    }

    private fun initProcessingOfPushNotification() {
        if (intent.hasExtra(EXTRA_NOTIFICATION)) {
            val notificationModel = intent.getParcelableExtra<NotificationModel>(EXTRA_NOTIFICATION)
            if (notificationModel != null) {
                notificationViewModel.saveNotification(notificationModel)
            }
        }
    }
}