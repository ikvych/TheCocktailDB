package com.ikvych.cocktail.splash

import android.os.Bundle
import android.os.Handler
import com.ikvych.cocktail.api.AuthStarter
import com.ikvych.cocktail.api.MainStarter
import com.ikvych.cocktail.prresentation.extension.observeOnce
import com.ikvych.cocktail.prresentation.ui.base.activity.BaseActivity
import com.ikvych.cocktail.splash.databinding.ActivitySplashBinding
import org.kodein.di.generic.instance
import kotlin.reflect.KClass

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_splash
    override val viewModelClass: KClass<SplashViewModel>
        get() = SplashViewModel::class
    private val mainStarter: MainStarter by instance()
    private val authStarter: AuthStarter by instance()

/*    private val notificationViewModel: NotificationViewModel by kodeinViewModel()*/

    override fun configureView(savedInstanceState: Bundle?) {
/*        initProcessingOfDynamicLink()
        initProcessingOfPushNotification()*/

        viewModel.checkForUser()
        viewModel.isUserPresentLiveData.observeOnce {
            if (it) {
                viewModel.refreshUser()
                Handler().postDelayed({
                    mainStarter.startMain()
                   /* val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)*/
                    finish()
                }, 1000)
            } else {
                Handler().postDelayed({
                    authStarter.startAuth()
/*                    val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                    startActivity(intent)*/
                    finish()
                }, 1000)
            }
        }
    }

/*    private fun initProcessingOfDynamicLink() {
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
    }*/
}