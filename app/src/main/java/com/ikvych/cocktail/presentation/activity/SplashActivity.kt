package com.ikvych.cocktail.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivitySplashBinding
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.presentation.extension.viewModels
import com.ikvych.cocktail.viewmodel.user.ProfileActivityViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import java.lang.NullPointerException
import kotlin.reflect.KClass

class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_splash
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    private val profileViewModel: ProfileActivityViewModel by viewModels()

    override fun configureView(savedInstanceState: Bundle?) {

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