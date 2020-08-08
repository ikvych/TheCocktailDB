package com.ikvych.cocktail.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivitySplashBinding
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.extension.observeOnce
import com.ikvych.cocktail.viewmodel.ProfileActivityViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlin.reflect.KClass

class SplashActivity : BaseActivity<ProfileActivityViewModel, ActivitySplashBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_splash
    override val viewModelClass: KClass<ProfileActivityViewModel>
        get() = ProfileActivityViewModel::class

    override fun configureView(savedInstanceState: Bundle?) {
        viewModel.checkForUser()
        viewModel.isUserPresentLiveData.observeOnce {
            if (it) {
                viewModel.refreshUser()
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
/*        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)*/
    }
}