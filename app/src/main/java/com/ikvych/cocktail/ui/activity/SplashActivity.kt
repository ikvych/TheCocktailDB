package com.ikvych.cocktail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.ikvych.cocktail.R
import com.ikvych.cocktail.ui.activity.base.BaseActivity

class SplashActivity : BaseActivity() {

    override var contentLayoutResId: Int = R.layout.activity_splash

    override fun configureView(savedInstanceState: Bundle?) {
        super.configureView(savedInstanceState)
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}