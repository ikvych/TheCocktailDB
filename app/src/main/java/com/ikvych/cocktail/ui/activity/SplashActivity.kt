package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.base.BaseActivity
import com.ikvych.cocktail.viewmodel.base.BaseViewModel

class SplashActivity : BaseActivity<BaseViewModel>() {

    override var contentLayoutResId: Int = R.layout.activity_splash
    override val viewModel: BaseViewModel by viewModels()

    override fun configureView(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}