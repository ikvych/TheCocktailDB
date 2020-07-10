package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivitySplashBinding
import com.ikvych.cocktail.ui.activity.base.BaseActivity
import com.ikvych.cocktail.viewmodel.SearchActivityViewModel
import com.ikvych.cocktail.viewmodel.base.BaseViewModel
import kotlin.reflect.KClass

class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_splash
    override val viewModelClass: KClass<BaseViewModel>
        get() = BaseViewModel::class

    override fun configureView(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}